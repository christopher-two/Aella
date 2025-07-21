# Documentación de Arquitectura de la Aplicación

## 1. Introducción

Este documento detalla la arquitectura implementada para la gestión de datos y la interfaz de usuario de la aplicación. El objetivo es proporcionar una guía clara y concisa sobre el diseño, los patrones utilizados y el flujo de datos, facilitando el mantenimiento y la escalabilidad futura del proyecto.

La arquitectura se ha diseñado siguiendo los principios de **Clean Architecture** y el patrón **MVVM (Model-View-ViewModel)**, promoviendo una clara separación de responsabilidades, alta cohesión y bajo acoplamiento entre las distintas capas.

## 2. Visión General de la Arquitectura

La aplicación se estructura en tres capas principales:

- **Capa de Datos (Data Layer)**: Responsable de la persistencia y el acceso a los datos. Es la única capa que sabe cómo se almacenan los datos (en este caso, en una base de datos Room).

- **Capa de Dominio (Domain Layer)**: Aunque no se ha creado una capa explícita con casos de uso, el Repositorio actúa como mediador, abstrayendo el origen de los datos de la lógica de negocio.

- **Capa de UI (UI Layer)**: Responsable de mostrar los datos y capturar las interacciones del usuario. No tiene conocimiento de dónde vienen los datos, solo los recibe del ViewModel.

## 3. Capa de Datos (Data Layer)

Esta capa gestiona toda la lógica de persistencia utilizando **Room**, una biblioteca de persistencia de Android Jetpack que actúa como una capa de abstracción sobre SQLite.

### 3.1. Entidades (@Entity)

**Propósito**: Son clases de datos que definen el esquema de las tablas en la base de datos. Cada instancia de una entidad corresponde a una fila en la tabla.

**Implementación**:
- `ProjectEntity`: Representa un proyecto en la base de datos.
- `ClientEntity`, `WorkerEntity`: Entidades adicionales que demuestran la escalabilidad del diseño.

**Clave**: Se utiliza `kotlinx.serialization` a través de `TypeConverters` para almacenar tipos complejos (como `List<String>`) como JSON en la base de datos.

### 3.2. DAOs (Data Access Objects - @Dao)

**Propósito**: Son interfaces que definen los métodos para interactuar con la base de datos. Aquí es donde se escriben las consultas SQL.

**Implementación**:
- `ProjectDao`: Contiene los métodos para `INSERT`, `UPDATE`, `DELETE` y `SELECT` de proyectos.

**Optimización Clave**: Se han implementado métodos de consulta paginados (`getProjects`, `searchProjects`) que utilizan `LIMIT` y `OFFSET`. Esto es crucial para el rendimiento, ya que la base de datos solo devuelve pequeños fragmentos de datos.

### 3.3. Base de Datos (@Database)

**Propósito**: Es la clase principal que une todas las partes de Room. Define las entidades que contiene, la versión del esquema y proporciona las instancias de los DAOs.

**Implementación**:
- `AppDatabase`: Implementada como un **Singleton** para garantizar una única instancia.

**Configuración Crítica**: Se utiliza `.allowMainThreadQueries()` durante la construcción, necesario para evitar fallos en la inicialización con Koin en una aplicación de escritorio.

### 3.4. Repositorio (Repository Pattern)

**Propósito**: Actúa como una fachada para la capa de datos. El resto de la aplicación (el `ViewModel`) solo se comunica con el repositorio.

**Implementación**:
- `ProjectRepository` (interfaz) y `ProjectRepositoryImpl` (implementación).
- Se encarga de llamar a los métodos paginados del DAO, calculando el offset según el número de página.

## 4. Inyección de Dependencias (Koin)

**Propósito**: Gestionar la creación y provisión de dependencias automáticamente, evitando acoplamientos manuales y facilitando las pruebas.

**Implementación**:
- `dataModule`: Módulo de Koin que define cómo construir cada componente de la capa de datos.
- Usa `single` para crear instancias únicas (Singleton) de la base de datos, DAOs y repositorios.

## 5. Capa de UI (MVVM)

Esta capa se encarga de todo lo visual y la interacción con el usuario.

### 5.1. ViewModel (`WorksViewModel`)

**Propósito**: Es el cerebro de la UI. Contiene la lógica de presentación y gestiona el estado de la pantalla.

**Implementación**:
- Recibe `ProjectRepository` vía inyección de dependencias.
- **Gestión de Estado**: Expone un `StateFlow<WorksState>`. La UI observa este flujo y se redibuja automáticamente cuando cambia.
- **Lógica de Paginación**: Lleva la cuenta de `currentPage` y solicita más datos al dispararse `LoadMoreProjects`.
- **Lógica de Búsqueda**: Usa un `Job` con delay (300ms) para implementar *debounce* y evitar búsquedas innecesarias.

### 5.2. Estado y Acciones

**Propósito**: Seguir un patrón de **flujo de datos unidireccional (UDF)**.

**Implementación**:
- `WorksState`: Clase de datos inmutable con toda la información necesaria para la UI (`isLoading`, `projects`, `searchQuery`, `hasMorePages`, etc.).
- `WorksAction`: `sealed interface` que define todas las acciones posibles del usuario (buscar, cargar más, etc.).

### 5.3. Vista (`WorksScreen`)

**Propósito**: Es un **Composable tonto** que solo muestra el estado y envía acciones al ViewModel.

**Implementación**:
- Usa `collectAsStateWithLifecycle` para observar el `StateFlow` del ViewModel de forma segura.
- Llama a `onAction` del ViewModel cuando el usuario interactúa (escribe, hace scroll, etc.).

## 6. Flujo de Datos: Ejemplo de Búsqueda Paginada

1. **Usuario**: Escribe `"Aella"` en la `SearchBar` de la `WorksScreen`.

2. **Vista**: La `SearchBar` invoca `onAction(WorksAction.OnSearchQueryChange("Aella"))`.

3. **ViewModel**:
   a. Recibe la acción.  
   b. Actualiza `searchQuery` en el `WorksState`.  
   c. Cancela cualquier `searchJob` anterior.  
   d. Lanza una nueva corrutina que espera 300ms.  
   e. Llama a `loadProjects(isNewSearch = true)`.

4. **loadProjects**:
   a. Determina que la página a cargar es la 1.  
   b. Llama a `projectRepository.searchProjects("Aella", page = 1, pageSize = 10)`.

5. **Repositorio**:
   a. Calcula el `offset = 0`.  
   b. Llama a `projectDao.searchProjects("%Aella%", limit = 10, offset = 0)`.

6. **DAO**: Ejecuta `SELECT ... WHERE name LIKE '%Aella%' ... LIMIT 10 OFFSET 0`.

7. **Base de Datos**: Devuelve hasta 10 `ProjectEntity`.

8. **Repositorio**: Mapea `ProjectEntity` a `ProjectData`.

9. **ViewModel**:
    - Actualiza `_state` con los nuevos proyectos.
    - Cambia `isLoading` a `false`.
    - Determina si `hasMorePages` es `true`.

10. **Vista**:
- `collectAsStateWithLifecycle` detecta el cambio.
- Compose redibuja `ProjectListScreen` con los resultados de la búsqueda.

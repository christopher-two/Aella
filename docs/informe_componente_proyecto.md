### **Guía de Uso: Componente de Lista de Proyectos con Búsqueda y Paginación**

#### **1\. Introducción**

Este documento detalla cómo utilizar el conjunto de componentes Composable creados para mostrar una lista de proyectos. El diseño se basa en tres principios clave: **modularidad**, **personalización** y **reutilización**. El resultado es un sistema de UI flexible que puede ser adaptado a diferentes necesidades y fuentes de datos sin modificar su código base.

La solución completa se compone de los siguientes elementos:

* **Modelos de Datos y Configuración:** Clases data class que definen la estructura de los datos y las opciones de personalización.  
* **Componentes de UI Modulares:** ProjectCard y SearchBar, que son reutilizables individualmente.  
* **Componente de Pantalla Principal:** ProjectListScreen, que orquesta los componentes de UI y la lógica de la lista.  
* **Implementación de Ejemplo:** ProjectListDemoScreen, que muestra cómo gestionar el estado y conectar todo.

#### **2\. Estructura del Código**

El código está organizado en 4 secciones numeradas para facilitar su comprensión:

1. **Modelos de Datos y Configuración:**  
   * ProjectStatus (enum): Define los posibles estados de un proyecto.  
   * ProjectData (data class): Define la estructura de un proyecto.  
   * ProjectListConfig (data class): Contiene todas las opciones para personalizar la apariencia (colores, formas, espaciados). **Esta es la clase clave para la modificación visual.**  
2. **Componentes Modulares de UI:**  
   * ProjectCard: Una tarjeta que muestra la información de un solo proyecto. Es independiente y puede ser usada en cualquier otra pantalla.  
   * SearchBar: Una barra de búsqueda genérica. También es totalmente reutilizable.  
3. **Componente Principal:**  
   * ProjectListScreen: La pantalla que une la barra de búsqueda y la lista LazyColumn. No contiene lógica de negocio; su única responsabilidad es mostrar los datos que recibe y notificar interacciones del usuario (como escribir en la búsqueda o llegar al final de la lista).  
4. **Ejemplo de Uso (Demo):**  
   * ProjectListDemoScreen: Un Composable que simula un entorno real. Aquí es donde se gestiona el estado (la lista de proyectos, la consulta de búsqueda, el estado de carga) y se implementa la lógica para la paginación y el filtrado.

#### **3\. Cómo Usarlo: Paso a Paso**

##### **Paso 1: Integración Rápida (Ver para Creer)**

Para ver el componente en acción inmediatamente, simplemente llama al Composable de demostración desde tu pantalla principal.

// En tu Composable App() o en la pantalla deseada  
import androidx.compose.material3.Surface  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.ui.Modifier

// ...

Surface(modifier \= Modifier.fillMaxSize()) {  
    // Esta es la única línea que necesitas para ver todo funcionando.  
    ProjectListDemoScreen()  
}

Esto renderizará la lista con datos de ejemplo, con la paginación y la búsqueda ya funcionales.

##### **Paso 2: Conectar tu Propia Fuente de Datos**

En una aplicación real, los datos vendrán de una API, una base de datos local o un ViewModel. Para conectar tus datos, usarás el componente ProjectListScreen directamente.

1. **Gestiona el estado:** Necesitarás gestionar el estado en tu propio Composable o, preferiblemente, en un ViewModel.  
   // En tu ViewModel o en tu Composable de pantalla  
   var allProjects by remember { mutableStateOf\<List\<ProjectData\>\>(emptyList()) }  
   var searchQuery by remember { mutableStateOf("") }  
   var isLoading by remember { mutableStateOf(false) }  
   // etc...

2. **Implementa la lógica de carga y filtrado:** Crea las funciones que se encargarán de obtener los datos y filtrarlos según la búsqueda.  
   // Lógica para cargar más proyectos (ej. llamar a una API)  
   fun loadMoreProjects() { /\* ... \*/ }

   // Lógica para filtrar los proyectos existentes  
   val filteredProjects \= allProjects.filter { /\* ... \*/ }

3. **Llama a ProjectListScreen con tu estado y lógica:**  
   // \--- Configuración (la misma que en la demo o una tuya) \---  
   val config \= remember { /\* ... tu configuración ... \*/ }

   // \--- Uso del Componente Principal \---  
   ProjectListScreen(  
       projects \= filteredProjects, // Tu lista filtrada  
       searchQuery \= searchQuery,   // Tu estado de búsqueda  
       onSearchQueryChange \= { newQuery \-\> searchQuery \= newQuery }, // Tu función para actualizar la búsqueda  
       onLoadMore \= ::loadMoreProjects, // Tu función para cargar más  
       isLoadingMore \= isLoading,       // Tu estado de carga  
       hasMorePages \= hasMorePages,     // Tu bandera para saber si hay más datos  
       config \= config                  // Tu objeto de configuración visual  
   )

##### **Paso 3: Personalización Visual Completa**

La gran ventaja de este diseño es que puedes cambiar drásticamente la apariencia **sin tocar la lógica de los componentes**. Todo se controla a través del objeto ProjectListConfig.

**Ejemplo: Crear un tema "Oscuro"**

Simplemente crea una nueva instancia de ProjectListConfig con diferentes colores y pásala al componente.

@Composable  
fun MyDarkModeScreen() {  
    // 1\. Define una nueva configuración con colores oscuros  
    val darkThemeConfig \= remember {  
        ProjectListConfig(  
            backgroundColor \= Color(0xFF1E293B), // Azul oscuro  
            cardColors \= ProjectListConfig.ProjectCardColors(  
                containerColor \= Color(0xFF334155), // Gris azulado  
                contentColor \= Color.White,  
                statusBadgeTextColor \= Color.White  
            ),  
            searchBarConfig \= ProjectListConfig.SearchBarConfig(  
                containerColor \= Color(0xFF334155),  
                contentColor \= Color.White,  
                placeholderColor \= Color.LightGray,  
                shape \= RoundedCornerShape(8.dp) // Forma más cuadrada  
            ),  
            paddings \= ProjectListConfig.ProjectListPaddings(  
                screenPadding \= PaddingValues(16.dp),  
                cardPadding \= PaddingValues(16.dp),  
                spacingBetweenCards \= 12.dp,  
                searchBarBottomPadding \= 16.dp  
            ),  
            typography \= MaterialTheme.typography // Puedes usar la misma tipografía  
        )  
    }

    // 2\. Usa el mismo componente \`ProjectListScreen\` pero con la nueva configuración  
    ProjectListScreen(  
        // ...pasa aquí tus datos como en el paso anterior...  
        config \= darkThemeConfig  
    )  
}

Al ejecutar el código anterior, obtendrás la misma funcionalidad pero con una apariencia completamente diferente, demostrando la flexibilidad del sistema.
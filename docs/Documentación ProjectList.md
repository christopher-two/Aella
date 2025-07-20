### **Documentación Técnica: Componente ProjectListScreen**

#### **1\. Resumen de Arquitectura**

El sistema de la lista de proyectos está diseñado siguiendo un patrón de **UI declarativa** y **desacoplamiento**, separando claramente las responsabilidades:

* **Capa de Datos (Externa):** No incluida en el componente. Se espera que sea gestionada por un ViewModel, Repository o una lógica de negocio similar. Esta capa es responsable de obtener, almacenar en caché y proveer los datos de los proyectos.  
* **Capa de Estado (en el Composable de pantalla):** El Composable que llama a ProjectListScreen (como ProjectListDemoScreen) actúa como un *State Holder*. Gestiona el estado de la UI (la lista de proyectos a mostrar, el texto de búsqueda, el estado de carga) y la lógica de negocio de la UI (filtrado, cuándo iniciar la paginación).  
* **Capa de UI (Componentes Puros):** ProjectListScreen, ProjectCard, y SearchBar son componentes de UI "puros" o "sin estado" (stateless). Su función es renderizar el estado que reciben como parámetros y emitir eventos de usuario (onSearchQueryChange, onLoadMore) hacia la capa de estado. No toman decisiones de negocio.

Este enfoque asegura que los componentes de UI sean altamente reutilizables, fáciles de previsualizar y testear, y que la lógica de negocio esté centralizada y sea fácil de razonar.

#### **2\. Flujo de Datos y Eventos**

El flujo de información es unidireccional, lo que previene inconsistencias de estado.

**Flujo de Datos (Estado hacia abajo):**

1. **ViewModel/State Holder:** Mantiene la lista completa de proyectos (allProjects) y la consulta de búsqueda (searchQuery).  
2. **Cálculo de Estado Derivado:** derivedStateOf calcula eficientemente la lista filtrada (filteredProjects) solo cuando allProjects o searchQuery cambian.  
3. **Paso de Props:** filteredProjects y searchQuery se pasan como parámetros a ProjectListScreen.  
4. **Renderizado:** ProjectListScreen pasa los datos relevantes a LazyColumn, ProjectCard y SearchBar para que se rendericen.

**Flujo de Eventos (Eventos hacia arriba):**

1. **Interacción del Usuario:** El usuario escribe en la SearchBar o se desplaza hasta el final de la LazyColumn.  
2. **Emisión de Eventos:**  
   * SearchBar invoca la lambda onQueryChange("nuevo texto").  
   * LazyColumn detecta que el último ítem es visible y, a través de un LaunchedEffect, invoca la lambda onLoadMore().  
3. **Manejo de Eventos:** El *State Holder* (ProjectListDemoScreen) recibe estas lambdas y actualiza su estado interno (ej. searchQuery \= "nuevo texto" o se inicia la corrutina para cargar más proyectos).  
4. **Recomposición:** El cambio de estado desencadena el flujo de datos hacia abajo, y la UI se actualiza para reflejar el nuevo estado.

#### **3\. API de Componentes Públicos**

##### **ProjectListScreen**

Este es el Composable principal que se debe utilizar para mostrar la pantalla completa.

| @Composablefun ProjectListScreen(    projects: List\<ProjectData\>,    searchQuery: String,    onSearchQueryChange: (String) \-\> Unit,    onLoadMore: () \-\> Unit,    isLoadingMore: Boolean,    hasMorePages: Boolean,    config: ProjectListConfig,    modifier: Modifier \= Modifier) |
| :---- |

* **projects: List\<ProjectData\>**: La lista de proyectos a mostrar *en el estado actual*. Debe ser la lista ya filtrada si la búsqueda está activa.  
* **searchQuery: String**: El valor actual del campo de búsqueda.  
* **onSearchQueryChange: (String) \-\> Unit**: Callback que se invoca cada vez que el valor de la búsqueda cambia. Es responsabilidad del llamador actualizar el estado.  
* **onLoadMore: () \-\> Unit**: Callback que se invoca cuando el usuario llega al final de la lista y se deben cargar más elementos. Debe ser idempotente dentro de lo posible, ya que el Composable no previene llamadas múltiples si las condiciones se cumplen rápidamente.  
* **isLoadingMore: Boolean**: Si es true, se mostrará un CircularProgressIndicator al final de la lista.  
* **hasMorePages: Boolean**: Controla si se debe intentar llamar a onLoadMore. Si es false, la paginación se detiene.  
* **config: ProjectListConfig**: El objeto de configuración para personalizar toda la apariencia de la pantalla.

##### **ProjectListConfig**

La data class que centraliza toda la personalización visual. Modificar una instancia de esta clase es la forma correcta de aplicar theming o adaptar el componente.

| @Stabledata class ProjectListConfig(    val backgroundColor: Color,    val cardColors: ProjectCardColors,    val searchBarConfig: SearchBarConfig,    val typography: Typography,    val paddings: ProjectListPaddings) |
| :---- |

* **@Stable**: Esta anotación es una optimización importante. Le informa al compilador de Compose que si ninguna de las propiedades de esta clase cambia, no es necesario recomponer los Composables que la usan, mejorando el rendimiento.

#### **4\. Guía de Integración con ViewModel**

Para integrar esto en una arquitectura MVVM, sigue este patrón:

| *// \--- En tu ViewModel \---*class ProjectViewModel : ViewModel() {    private val \_uiState \= MutableStateFlow(ProjectUiState())    val uiState: StateFlow\<ProjectUiState\> \= \_uiState.asStateFlow()    *// Lógica para cargar, buscar, etc.*    fun onSearchQueryChanged(query: String) {        \_uiState.update { it.copy(searchQuery \= query) }    }    fun loadNextPage() {        *// Lógica de repositorio para obtener más datos...*    }}data class ProjectUiState(    val allProjects: List\<ProjectData\> \= emptyList(),    val searchQuery: String \= "",    val isLoading: Boolean \= false,    */\* ... \*/*)*// \--- En tu Composable de Pantalla \---*@Composablefun MyProjectRoute(viewModel: ProjectViewModel \= viewModel()) {    val uiState by viewModel.uiState.collectAsState()    val filteredProjects \= remember(uiState.allProjects, uiState.searchQuery) {        *// Lógica de filtrado aquí...*    }    ProjectListScreen(        projects \= filteredProjects,        searchQuery \= uiState.searchQuery,        onSearchQueryChange \= viewModel::onSearchQueryChanged,        onLoadMore \= viewModel::loadNextPage,        *// ...otros parámetros...*    )} |
| :---- |

#### **5\. Consideraciones de Rendimiento**

Se han implementado varias optimizaciones clave:

* **LazyColumn**: Solo compone y renderiza los elementos que son visibles en la pantalla, esencial para listas largas.  
* **key en itemsIndexed**: key \= { \_, project \-\> project.id }. Proporcionar una clave única y estable permite a Compose identificar elementos de manera inteligente entre recomposiciones. Esto evita que se recompongan elementos que no han cambiado y permite animaciones más fluidas.  
* **derivedStateOf**: Se usa en la demo para asegurar que el filtrado de la lista solo se ejecute cuando las dependencias (allProjects o searchQuery) cambian, no en cada recomposición.  
* **Inmutabilidad:** El uso de data class con val promueve la inmutabilidad. Compose es mucho más eficiente al detectar cambios en objetos inmutables que en mutables.  
* **@Stable**: Como se mencionó, esta anotación en la clase de configuración ayuda a Compose a omitir recomposiciones innecesarias.
### **Documentación Técnica: Componente ProjectListScreen**

---

#### **1. Resumen de Arquitectura**

El sistema de la lista de proyectos sigue un patrón de **UI declarativa** y **desacoplamiento**, separando claramente las responsabilidades:

- **Capa de Datos (Externa):** Gestionada fuera del componente (ViewModel, Repository). Encargada de obtener y proveer los datos.
- **Capa de Estado:** El Composable llamador (ej. ProjectListDemoScreen) actúa como *State Holder*, gestionando estado y lógica de UI.
- **Capa de UI (Componentes Puros):** ProjectListScreen, ProjectCard, y SearchBar. Componentes sin estado, responsables solo de renderizar y emitir eventos.

Este diseño permite alta reutilización, pruebas simples y separación clara de lógica y presentación.

---

#### **2. Flujo de Datos y Eventos**

**Flujo de Datos (de arriba hacia abajo):**

1. ViewModel mantiene `allProjects` y `searchQuery`.
2. `derivedStateOf` calcula `filteredProjects`.
3. `filteredProjects` y `searchQuery` se pasan a `ProjectListScreen`.
4. `ProjectListScreen` renderiza la UI con LazyColumn, ProjectCard y SearchBar.

**Flujo de Eventos (de abajo hacia arriba):**

1. El usuario interactúa con la `SearchBar` o alcanza el final de la lista.
2. Se disparan callbacks:
    - `onSearchQueryChange("nuevo texto")`
    - `onLoadMore()`
3. El *State Holder* actualiza el estado interno.
4. Se recomputa el estado derivado y se vuelve a renderizar la UI.

---

#### **3. API de Componentes Públicos**

##### **ProjectListScreen**

```kotlin
@Composable
fun ProjectListScreen(
    projects: List<ProjectData>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    config: ProjectListConfig,
    modifier: Modifier = Modifier
)
```

- `projects`: Lista ya filtrada de proyectos.
- `searchQuery`: Texto actual de búsqueda.
- `onSearchQueryChange`: Callback para cambios en búsqueda.
- `onLoadMore`: Callback para paginación.
- `isLoadingMore`: Muestra un spinner si es `true`.
- `hasMorePages`: Controla si se puede paginar.
- `config`: Configuración visual.
- `modifier`: Para composición externa.

##### **ProjectListConfig**

```kotlin
@Stable
data class ProjectListConfig(
    val backgroundColor: Color,
    val cardColors: ProjectCardColors,
    val searchBarConfig: SearchBarConfig,
    val typography: Typography,
    val paddings: ProjectListPaddings
)
```

- Centraliza la personalización visual.
- `@Stable` mejora el rendimiento evitando recomposiciones innecesarias.

---

#### **4. Guía de Integración con ViewModel**

```kotlin
// --- En tu ViewModel ---
class ProjectViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun loadNextPage() {
        // lógica para cargar más datos
    }
}

data class ProjectUiState(
    val allProjects: List<ProjectData> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
    // ...
)

// --- En tu Composable de Pantalla ---
@Composable
fun MyProjectRoute(viewModel: ProjectViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val filteredProjects = remember(uiState.allProjects, uiState.searchQuery) {
        // lógica de filtrado
    }

    ProjectListScreen(
        projects = filteredProjects,
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onLoadMore = viewModel::loadNextPage,
        isLoadingMore = uiState.isLoading,
        hasMorePages = true, // ejemplo
        config = defaultConfig()
    )
}
```

---

#### **5. Consideraciones de Rendimiento**

- **LazyColumn:** Composición eficiente de listas largas.
- **key en itemsIndexed:** `key = { _, project -> project.id }` para evitar recomposición innecesaria.
- **derivedStateOf:** Minimiza cálculos innecesarios.
- **Inmutabilidad:** Uso de `val` y `data class` para facilitar detección de cambios.
- **@Stable:** Mejora de rendimiento al evitar recomposición innecesaria.

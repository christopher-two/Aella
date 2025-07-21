package org.christophertwo.aella.ui.screen.works

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.christophertwo.aella.data.room.repository.ProjectRepository

/**
 * ViewModel para la pantalla "Works".
 * Gestiona el estado de la UI y la lógica de negocio, incluyendo la paginación
 * y la búsqueda optimizada.
 *
 * @param projectRepository El repositorio para acceder a los datos de los proyectos.
 */
class WorksViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorksState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    companion object {
        private const val PAGE_SIZE = 10
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    init {
        // Carga la primera página de proyectos al iniciar el ViewModel.
        loadProjects(isNewSearch = true)
    }

    /**
     * Maneja las acciones que vienen de la UI.
     */
    fun onAction(action: WorksAction) {
        when (action) {
            is WorksAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                // Cancela la búsqueda anterior y lanza una nueva después de un breve retraso.
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(SEARCH_DEBOUNCE_MS) // Debounce para no sobrecargar la BD.
                    loadProjects(isNewSearch = true)
                }
            }

            is WorksAction.LoadMoreProjects -> {
                // Carga la siguiente página de resultados.
                loadProjects(isNewSearch = false)
            }
        }
    }

    /**
     * Función central para cargar proyectos, ya sea una nueva búsqueda o la siguiente página.
     * @param isNewSearch Indica si la carga debe reiniciar la lista (búsqueda nueva) o añadir a ella.
     */
    private fun loadProjects(isNewSearch: Boolean) {
        val currentState = _state.value
        // Previene cargas múltiples si ya hay una en progreso o si no hay más páginas.
        if (currentState.isLoadingMore || (!isNewSearch && !currentState.hasMorePages)) {
            return
        }

        viewModelScope.launch {
            // Determina la página a cargar. Si es una nueva búsqueda, es la página 1.
            val pageToLoad = if (isNewSearch) 1 else currentState.currentPage + 1

            // Actualiza el estado para mostrar los indicadores de carga apropiados.
            _state.update {
                it.copy(
                    isLoading = isNewSearch,
                    isLoadingMore = !isNewSearch,
                    currentPage = pageToLoad
                )
            }

            val projectsResult = try {
                val query = _state.value.searchQuery
                if (query.isBlank()) {
                    projectRepository.getProjects(page = pageToLoad, pageSize = PAGE_SIZE)
                } else {
                    projectRepository.searchProjects(
                        query = query,
                        page = pageToLoad,
                        pageSize = PAGE_SIZE
                    )
                }
            } catch (e: Exception) {
                // En un caso real, aquí se manejaría el error (ej: mostrar un snackbar).
                e.printStackTrace()
                emptyList()
            }

            // Actualiza el estado final con los nuevos datos.
            _state.update {
                val currentProjects = if (isNewSearch) emptyList() else it.projects
                it.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    projects = currentProjects + projectsResult,
                    // Si los resultados obtenidos son menos que el tamaño de la página,
                    // significa que hemos llegado al final.
                    hasMorePages = projectsResult.size == PAGE_SIZE
                )
            }
        }
    }
}
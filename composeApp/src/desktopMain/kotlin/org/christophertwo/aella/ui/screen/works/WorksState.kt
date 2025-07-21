package org.christophertwo.aella.ui.screen.works

import org.christophertwo.aella.utils.model.ProjectData

/**
 * Representa el estado de la UI para la pantalla de "Works".
 *
 * @property isLoading Indica si la carga inicial de datos está en progreso.
 * @property projects La lista de proyectos que se muestra actualmente en la UI.
 * @property searchQuery El texto actual en la barra de búsqueda.
 * @property isLoadingMore Indica si se están cargando más proyectos (paginación).
 * @property hasMorePages Indica si hay más páginas de proyectos para cargar.
 * @property currentPage El número de la última página cargada.
 */
data class WorksState(
    val isLoading: Boolean = true,
    val projects: List<ProjectData> = emptyList(),
    val searchQuery: String = "",
    val isLoadingMore: Boolean = false,
    val hasMorePages: Boolean = true,
    val currentPage: Int = 0
)

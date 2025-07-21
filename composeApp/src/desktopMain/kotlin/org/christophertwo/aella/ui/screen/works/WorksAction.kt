package org.christophertwo.aella.ui.screen.works

/**
 * Define las acciones que el usuario puede realizar en la pantalla de "Works".
 */
sealed interface WorksAction {
    /**
     * Se dispara cuando el usuario llega al final de la lista para cargar más proyectos.
     */
    object LoadMoreProjects : WorksAction

    /**
     * Se dispara cada vez que el texto en la barra de búsqueda cambia.
     * @property query El nuevo texto de búsqueda.
     */
    data class OnSearchQueryChange(val query: String) : WorksAction
}

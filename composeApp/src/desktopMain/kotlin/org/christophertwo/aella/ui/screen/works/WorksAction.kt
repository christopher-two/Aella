package org.christophertwo.aella.ui.screen.works

sealed interface WorksAction {
    object LoadMoreProjects : WorksAction
}
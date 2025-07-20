package org.christophertwo.aella.utils.routes

sealed class RoutesHome(val route: String) {
    object Workspaces : RoutesHome("workspaces")
    object Eva : RoutesHome("eva")
    object Works : RoutesHome("works")
}
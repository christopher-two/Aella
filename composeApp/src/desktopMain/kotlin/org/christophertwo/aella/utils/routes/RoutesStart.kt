package org.christophertwo.aella.utils.routes

sealed class RoutesStart(val route: String) {
    object Home : RoutesStart("home")
}
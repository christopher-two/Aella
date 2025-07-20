package org.christophertwo.aella.ui.screen.home

import org.christophertwo.aella.utils.model.NavItem

data class HomeState(
    val selectedItem: NavItem = NavItem().getItems()[0],
    val navItems: List<NavItem> = NavItem().getItems(),
    val isConfigDialogOpen: Boolean = false,
    val stateTheme: List<String> = listOf("Obscuro", "Claro"),
    val stateColorTheme: List<String> = listOf("Green", "Blue", "Purple"),
    val themeSelectedInDialog: String = "Claro",
    val colorThemeSelectedInDialog: String = "Green"
)

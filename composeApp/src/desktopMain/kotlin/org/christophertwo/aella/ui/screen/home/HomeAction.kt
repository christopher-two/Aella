package org.christophertwo.aella.ui.screen.home

import org.christophertwo.aella.utils.model.NavItem

sealed interface HomeAction {
    data class SelectNavItem(val navItem: NavItem) : HomeAction
    object CloseConfigDialog : HomeAction
    object OpenConfigDialog : HomeAction
    data class ThemeSelectionChangedInDialog(val newTheme: String) : HomeAction
    data class ColorThemeSelectionChangedInDialog(val newColorTheme: String) : HomeAction
    object Save : HomeAction
}

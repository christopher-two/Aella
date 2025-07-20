package org.christophertwo.aella.ui.screen.home

import org.christophertwo.aella.utils.model.NavItem

sealed interface HomeAction {
    data class SelectNavItem(val navItem: NavItem) : HomeAction
}
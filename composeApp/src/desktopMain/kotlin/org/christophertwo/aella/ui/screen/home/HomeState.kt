package org.christophertwo.aella.ui.screen.home

import org.christophertwo.aella.utils.model.NavItem

data class HomeState(
    val selectedItem: NavItem = NavItem().getItems()[0],
    val navItems: List<NavItem> = NavItem().getItems()
)
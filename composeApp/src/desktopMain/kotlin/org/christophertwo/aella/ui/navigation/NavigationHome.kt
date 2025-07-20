package org.christophertwo.aella.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.christophertwo.aella.utils.model.NavItem
import org.christophertwo.aella.utils.routes.RoutesHome

@Composable
fun NavigationHome(
    navController: NavHostController,
    startDestination: String,
    navItems: List<NavItem>
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(RoutesHome.Workspaces.route) {
            navItems[0].screenContent()
        }
        composable(RoutesHome.Eva.route) {
            navItems[1].screenContent()
        }
        composable(RoutesHome.Works.route) {
            navItems[2].screenContent()
        }
    }
}
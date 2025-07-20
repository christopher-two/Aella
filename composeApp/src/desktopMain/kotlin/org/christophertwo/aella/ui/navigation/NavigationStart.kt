package org.christophertwo.aella.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.christophertwo.aella.ui.screen.home.HomeRoot
import org.christophertwo.aella.utils.routes.RoutesStart

@Composable
fun NavigationStart() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RoutesStart.Home.route
    ) {
        composable(RoutesStart.Home.route) {
            HomeRoot()
        }
    }
}
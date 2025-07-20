package org.christophertwo.aella.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.christophertwo.aella.ui.navigation.NavigationHome
import org.christophertwo.aella.utils.routes.RoutesHome
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val navController = rememberNavController()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        NavigationRail(
            containerColor = colorScheme.surfaceContainer,
            contentColor = colorScheme.onSurface,
            modifier = Modifier
                .width(35.sdp)
        ) {
            state.navItems.forEachIndexed { index, item ->
                NavigationRailItem(
                    modifier = Modifier.padding(bottom = 5.sdp),
                    selected = state.selectedItem == item,
                    onClick = {
                        onAction(HomeAction.SelectNavItem(item))
                        navController.navigate(item.route.route) {
                            popUpTo(RoutesHome.Workspaces.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(9.sdp)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 4.ssp
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
        NavigationHome(
            navController = navController,
            startDestination = RoutesHome.Workspaces.route,
            navItems = state.navItems
        )
    }
}
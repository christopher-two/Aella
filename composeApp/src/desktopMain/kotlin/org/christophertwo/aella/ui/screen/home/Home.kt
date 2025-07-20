package org.christophertwo.aella.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Settings
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.christophertwo.aella.ui.components.SettingsDialog
import org.christophertwo.aella.ui.navigation.NavigationHome
import org.christophertwo.aella.utils.config.SettingItem
import org.christophertwo.aella.utils.config.SettingsDialogConfig
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
            modifier = Modifier.width(35.sdp)
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
            Spacer(modifier = Modifier.weight(1f))
            NavigationRailItem(
                modifier = Modifier.padding(bottom = 5.sdp),
                selected = false,
                onClick = {
                    onAction(HomeAction.OpenConfigDialog)
                },
                icon = {
                    Icon(
                        imageVector = EvaIcons.Outline.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(9.sdp)
                    )
                },
                alwaysShowLabel = false
            )
        }
        NavigationHome(
            navController = navController,
            startDestination = RoutesHome.Workspaces.route,
            navItems = state.navItems
        )
    }

    if (state.isConfigDialogOpen) {
        // Creamos un estado mutable para el diálogo que se actualiza con la acción
        val choiceStateTheme = mutableStateOf(state.themeSelectedInDialog)
        val choiceStateThemeColor = mutableStateOf(state.colorThemeSelectedInDialog)

        SettingsDialog(
            onDismissRequest = { onAction(HomeAction.CloseConfigDialog) },
            title = "Ajustes",
            items = listOf(
                SettingItem.ChoiceSetting(
                    title = "Tema",
                    state = choiceStateTheme,
                    options = state.stateTheme,
                    onStateChange = { onAction(HomeAction.ThemeSelectionChangedInDialog(it)) }
                ),
                SettingItem.ChoiceSetting(
                    title = "Color del tema",
                    state = choiceStateThemeColor,
                    options = state.stateColorTheme,
                    onStateChange = { onAction(HomeAction.ColorThemeSelectionChangedInDialog(it)) }
                )
            ),
            config = SettingsDialogConfig(
                colors = SettingsDialogConfig.DialogColors(
                    containerColor = colorScheme.surfaceContainer,
                    titleContentColor = colorScheme.onSurface,
                    dividerColor = colorScheme.outline
                ),
                typography = SettingsDialogConfig.DialogTypography(titleTextStyle = MaterialTheme.typography.titleLarge),
                dimensions = SettingsDialogConfig.DialogDimensions(),
                shape = RoundedCornerShape(10.dp),
            ),
            buttons = {
                OutlinedButton(
                    onClick = { onAction(HomeAction.CloseConfigDialog) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { onAction(HomeAction.Save) }
                ) {
                    Text("Guardar")
                }
            },
        )
    }
}

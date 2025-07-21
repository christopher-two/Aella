package org.christophertwo.aella

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.christophertwo.aella.di.PreferenceModule
import org.christophertwo.aella.di.ViewModelModules
import org.christophertwo.aella.di.dataModule
import org.christophertwo.aella.ui.navigation.NavigationStart
import org.christophertwo.aella.ui.theme.ThemeApp
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

fun main() = application {
    KoinApplication(
        application = {
            printLogger()
            modules(ViewModelModules, PreferenceModule, dataModule)
        }
    ) {
        val mainViewModel: MainViewModel = koinInject()
        val isDark by mainViewModel.isDarkTheme.collectAsState()
        val seedColor by mainViewModel.themeColor.collectAsState()

        Window(
            onCloseRequest = ::exitApplication,
            title = "Aella",
        ) {
            ThemeApp(
                seedColor = seedColor,
                useDarkTheme = isDark
            ) {
                NavigationStart()
            }
        }
    }
}

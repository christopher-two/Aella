package org.christophertwo.aella

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.christophertwo.aella.di.ViewModelModules
import org.christophertwo.aella.ui.navigation.NavigationStart
import org.christophertwo.aella.ui.theme.ThemeApp
import org.koin.core.context.startKoin

@OptIn(InternalComposeUiApi::class)
fun main() = application {
    startKoin {
        printLogger()
        modules(ViewModelModules)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Aella",
    ) {
        var isDark: Boolean by remember { mutableStateOf(true) }
        ThemeApp(
            seedColor = Color(0xFF63A002),
            useDarkTheme = isDark
        ) {
            NavigationStart()
        }
    }
}
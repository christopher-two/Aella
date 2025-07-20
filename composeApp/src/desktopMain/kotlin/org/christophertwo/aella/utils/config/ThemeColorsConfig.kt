package org.christophertwo.aella.utils.config

import androidx.compose.ui.graphics.Color

sealed class ThemeColorsConfig(val color: Color, val label: String) {
    object Green : ThemeColorsConfig(Color(0xFF63A002), "Green")
    object Blue : ThemeColorsConfig(Color(0xff2f57ea), "Blue")
    object Purple : ThemeColorsConfig(Color(0xff4e02a0), "Purple")

    companion object {
        fun getColor(label: String): ThemeColorsConfig {
            return when (label) {
                "Green" -> Green
                "Blue" -> Blue
                "Purple" -> Purple
                else -> Green
            }
        }
    }
}

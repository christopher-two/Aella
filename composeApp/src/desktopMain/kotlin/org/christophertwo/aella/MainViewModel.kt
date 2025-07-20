package org.christophertwo.aella

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.christophertwo.aella.data.preference.PreferenceSettingsInterface
import org.christophertwo.aella.utils.config.ThemeColorsConfig
import org.christophertwo.aella.utils.objects.PreferenceKey.THEME_COLOR_KEY
import org.christophertwo.aella.utils.objects.PreferenceKey.THEME_KEY

/**
 * ViewModel para gestionar el estado global de la aplicación, como el tema.
 */
class MainViewModel(
    private val preferenceSettings: PreferenceSettingsInterface
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _themeColor = MutableStateFlow(value = ThemeColorsConfig.Green.color)
    val themeColor = _themeColor.asStateFlow()

    init {
        loadInitialTheme()
    }

    private fun loadInitialTheme() {
        viewModelScope.launch {
            val savedTheme = preferenceSettings.getPreference(THEME_KEY)
            val themeColor = preferenceSettings.getPreference(THEME_COLOR_KEY)
            _themeColor.value = ThemeColorsConfig.getColor(themeColor ?: ThemeColorsConfig.Green.label).color
            _isDarkTheme.value = savedTheme == "Obscuro"
        }
    }

    /**
     * Cambia el tema actual y lo guarda en las preferencias.
     */
    fun changeTheme(theme: String) {
        viewModelScope.launch {
            // Guardamos la preferencia
            preferenceSettings.setPreference(THEME_KEY, theme)
            // Actualizamos el estado que la UI está observando
            _isDarkTheme.update { theme == "Obscuro" }
        }
    }

    /**
     * Cambia el color del tema actual y lo guarda en las preferencias.
     */
    fun changeThemeColor(color: String) {
        viewModelScope.launch {
            // Guardamos la preferencia
            preferenceSettings.setPreference(THEME_COLOR_KEY, color)
            // Actualizamos el estado que la UI está observando
            _themeColor.update { ThemeColorsConfig.getColor(color).color }
        }
    }

    /**
     * Obtiene el nombre del tema actual ("Obscuro" o "Claro")
     */
    fun getCurrentThemeName(): String {
        return if (_isDarkTheme.value) "Obscuro" else "Claro"
    }
}

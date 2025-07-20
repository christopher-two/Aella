package org.christophertwo.aella.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.christophertwo.aella.MainViewModel

class HomeViewModel(
    // Inyectamos el MainViewModel para comunicarnos con él
    private val mainViewModel: MainViewModel
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SelectNavItem -> {
                _state.update { it.copy(selectedItem = action.navItem) }
            }

            is HomeAction.CloseConfigDialog -> {
                _state.update { it.copy(isConfigDialogOpen = false) }
            }

            is HomeAction.OpenConfigDialog -> {
                // Al abrir el diálogo, cargamos el tema actual desde MainViewModel
                _state.update {
                    it.copy(
                        isConfigDialogOpen = true,
                        themeSelectedInDialog = mainViewModel.getCurrentThemeName()
                    )
                }
            }

            is HomeAction.Save -> {
                // Al guardar, le decimos al MainViewModel que cambie el tema
                mainViewModel.changeTheme(_state.value.themeSelectedInDialog)
                mainViewModel.changeThemeColor(_state.value.colorThemeSelectedInDialog)
                // Y cerramos el diálogo
                _state.update { it.copy(isConfigDialogOpen = false) }
            }

            is HomeAction.ThemeSelectionChangedInDialog -> {
                // Actualizamos el estado temporal del tema en el diálogo
                _state.update { it.copy(themeSelectedInDialog = action.newTheme) }
            }

            is HomeAction.ColorThemeSelectionChangedInDialog -> {
                // Actualizamos el estado temporal del tema en el diálogo
                _state.update { it.copy(colorThemeSelectedInDialog = action.newColorTheme) }
            }
        }
    }
}

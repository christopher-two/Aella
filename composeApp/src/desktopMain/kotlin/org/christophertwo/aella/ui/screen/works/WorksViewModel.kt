package org.christophertwo.aella.ui.screen.works

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorksViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(WorksState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = WorksState()
        )

    fun onAction(action: WorksAction) {
        when (action) {
            is WorksAction.LoadMoreProjects -> {
                viewModelScope.launch {
                    delay(100)
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            hasMorePages = false
                        )
                    }
                }
            }
        }
    }

}
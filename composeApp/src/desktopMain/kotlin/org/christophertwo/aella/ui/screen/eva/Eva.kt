package org.christophertwo.aella.ui.screen.eva

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EvaRoot(
    viewModel: EvaViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EvaScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun EvaScreen(
    state: EvaState,
    onAction: (EvaAction) -> Unit,
) {

}
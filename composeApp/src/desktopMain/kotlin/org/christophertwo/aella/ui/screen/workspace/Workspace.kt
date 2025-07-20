package org.christophertwo.aella.ui.screen.workspace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import network.chaintech.sdpcomposemultiplatform.sdp
import org.christophertwo.aella.ui.components.ProjectListScreen
import org.christophertwo.aella.utils.config.ProjectListConfig
import org.christophertwo.aella.utils.model.ProjectData
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkspaceRoot(
    viewModel: WorkspaceViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WorkspaceScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun WorkspaceScreen(
    state: WorkspaceState,
    onAction: (WorkspaceAction) -> Unit,
) {
}
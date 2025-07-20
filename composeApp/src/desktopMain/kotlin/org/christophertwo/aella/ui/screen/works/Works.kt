package org.christophertwo.aella.ui.screen.works

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun WorksRoot(
    viewModel: WorksViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WorksScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun WorksScreen(
    state: WorksState,
    onAction: (WorksAction) -> Unit,
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = colorScheme.onSurface,
        modifier = Modifier.fillMaxSize(),
    ) {
        ProjectListScreen(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.sdp),
            projects = ProjectData.generateDummyProjects(15, 10),
            onLoadMore = { onAction(WorksAction.LoadMoreProjects) },
            isLoadingMore = state.isLoadingMore,
            hasMorePages = state.hasMorePages,
            config = ProjectListConfig(
                backgroundColor = Color.Transparent,
                cardColors = ProjectListConfig.ProjectCardColors(
                    containerColor = colorScheme.surfaceContainer,
                    contentColor = colorScheme.onSurface,
                    statusBadgeTextColor = colorScheme.onPrimary
                ),
                typography = typography,
                paddings = ProjectListConfig.ProjectListPaddings(
                    screenPadding = PaddingValues(16.dp),
                    cardPadding = PaddingValues(16.dp),
                    spacingBetweenCards = 16.dp,
                    searchBarBottomPadding = 16.dp
                ),
                searchBarConfig = ProjectListConfig.SearchBarConfig(
                    containerColor = colorScheme.surfaceContainer,
                    contentColor = colorScheme.onSurface,
                    placeholderColor = colorScheme.onSurface.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(50)
                )
            ),
            searchQuery = "",
            onSearchQueryChange = {

            }
        )
    }
}
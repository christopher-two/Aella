package org.christophertwo.aella.ui.screen.works

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            ProjectListScreen(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 10.sdp),
                projects = state.projects, // Usamos los proyectos del estado
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
                searchQuery = state.searchQuery, // Usamos el query del estado
                onSearchQueryChange = { newQuery ->
                    onAction(WorksAction.OnSearchQueryChange(newQuery))
                },
                saveProject = {
                    // Aquí iría la lógica para guardar un proyecto si fuera necesario
                }
            )
        }
    }
}
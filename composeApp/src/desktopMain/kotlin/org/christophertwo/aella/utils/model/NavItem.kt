package org.christophertwo.aella.utils.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Archive
import compose.icons.evaicons.outline.Briefcase
import compose.icons.evaicons.outline.Star
import org.christophertwo.aella.ui.screen.works.WorksRoot
import org.christophertwo.aella.ui.screen.workspace.WorkspaceRoot
import org.christophertwo.aella.utils.routes.RoutesHome

data class NavItem(
    val label: String = "",
    val icon: ImageVector = EvaIcons.Outline.Briefcase,
    val route: RoutesHome = RoutesHome.Workspaces,
    val screenContent: @Composable () -> Unit = {}
) {
    fun getItems(): List<NavItem> {
        return listOf(
            NavItem(
                label = "Workspaces",
                icon = EvaIcons.Outline.Briefcase,
                route = RoutesHome.Workspaces,
                screenContent = { WorkspaceRoot() }
            ),
            NavItem(
                label = "Eva",
                icon = EvaIcons.Outline.Star,
                route = RoutesHome.Eva,
                screenContent = { }
            ),
            NavItem(
                label = "Works",
                icon = EvaIcons.Outline.Archive,
                route = RoutesHome.Works,
                screenContent = { WorksRoot() }
            )
        )
    }
}
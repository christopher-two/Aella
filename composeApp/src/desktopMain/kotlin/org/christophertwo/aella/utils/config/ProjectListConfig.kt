package org.christophertwo.aella.utils.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Typography
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Agrupa todas las opciones de personalización visual para el componente de la lista.
 * Permite modificar la apariencia sin cambiar la lógica del componente.
 *
 * @property backgroundColor Color de fondo de toda la pantalla.
 * @property cardColors Colores específicos para las tarjetas de proyecto.
 * @property typography Instancia de [Typography] para usar estilos de texto consistentes.
 * @property paddings Define los espaciados y paddings usados en el componente.
 */
@Stable
data class ProjectListConfig(
    val backgroundColor: Color,
    val cardColors: ProjectCardColors,
    val typography: Typography,
    val paddings: ProjectListPaddings,
    val searchBarConfig: SearchBarConfig
) {
    /** Colores para el componente [ProjectCard]. */
    @Stable
    data class ProjectCardColors(
        val containerColor: Color,
        val contentColor: Color,
        val statusBadgeTextColor: Color
    )

    /** Paddings y espaciados para el layout. */
    @Stable
    data class ProjectListPaddings(
        val screenPadding: PaddingValues,
        val cardPadding: PaddingValues,
        val spacingBetweenCards: Dp,
        val searchBarBottomPadding: Dp,
    )

    /** Configuración visual para el componente [SearchBar]. */
    @Stable
    data class SearchBarConfig(
        val containerColor: Color,
        val contentColor: Color,
        val placeholderColor: Color,
        val shape: RoundedCornerShape
    )
}
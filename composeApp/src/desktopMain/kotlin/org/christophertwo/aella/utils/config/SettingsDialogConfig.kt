package org.christophertwo.aella.utils.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class SettingsDialogConfig(
    val colors: DialogColors,
    val dimensions: DialogDimensions,
    val shape: Shape,
    val typography: DialogTypography
) {
    /**
     * Define los colores para los componentes del dialog.
     *
     * @property containerColor El color de fondo principal del dialog.
     * @property titleContentColor El color del texto del título.
     * @property dividerColor El color de las líneas separadoras.
     */
    @Stable
    data class DialogColors(
        val containerColor: Color,
        val titleContentColor: Color,
        val dividerColor: Color
    )

    /**
     * Define las dimensiones y espaciados del dialog.
     *
     * @property dialogMaxWidth El ancho máximo que el dialog puede ocupar. Evita que se estire demasiado en pantallas grandes.
     * @property dialogMaxHeight El alto máximo que el dialog puede ocupar. Permite el scroll si el contenido es muy largo.
     * @property contentPadding El padding interno para el área de contenido principal.
     * @property titlePadding El padding para el área del título.
     * @property buttonsPadding El padding para la fila de botones de acción.
     */
    @Stable
    data class DialogDimensions(
        val dialogMaxWidth: Dp = 800.dp,
        val dialogMaxHeight: Dp = 600.dp,
        val contentPadding: PaddingValues = PaddingValues(16.dp),
        val titlePadding: PaddingValues = PaddingValues(16.dp),
        val buttonsPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )

    /**
     * Define los estilos de texto utilizados en el dialog.
     *
     * @property titleTextStyle El estilo tipográfico para el título del dialog.
     */
    @Stable
    data class DialogTypography(
        val titleTextStyle: TextStyle
    )

    companion object {
        /**
         * Función de utilidad para crear una configuración de dialog por defecto
         * utilizando los valores del tema actual de Material 3.
         */
        @Composable
        fun getDefaultSettingsDialogConfig(): SettingsDialogConfig {
            return SettingsDialogConfig(
                colors = DialogColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    dividerColor = MaterialTheme.colorScheme.outlineVariant
                ),
                dimensions = DialogDimensions(),
                shape = MaterialTheme.shapes.large,
                typography = DialogTypography(
                    titleTextStyle = MaterialTheme.typography.headlineSmall
                )
            )
        }
    }
}

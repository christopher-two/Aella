package org.christophertwo.aella.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowDown
import org.christophertwo.aella.utils.config.SettingItem
import org.christophertwo.aella.utils.config.SettingItem.ChoiceSetting
import org.christophertwo.aella.utils.config.SettingItem.SliderSetting
import org.christophertwo.aella.utils.config.SettingItem.TextEntrySetting
import org.christophertwo.aella.utils.config.SettingItem.ToggleSetting
import org.christophertwo.aella.utils.config.SettingsDialogConfig

/**
 * Un dialog de configuración genérico y adaptable para Compose Multiplatform.
 *
 * Esta versión es data-driven. Acepta una lista de `SettingItem` y renderiza
 * automáticamente el control de UI apropiado para cada uno. Esto abstrae
 * por completo la implementación de la UI, permitiendo al desarrollador
 * centrarse únicamente en los datos de configuración.
 *
 * @param onDismissRequest Se invoca cuando el usuario intenta cerrar el dialog.
 * @param title El texto que se mostrará como título del dialog.
 * @param items La lista de modelos de configuración a renderizar.
 * @param config La configuración de estilo del dialog.
 * @param buttons Un slot Composable para los botones de acción (ej. "Guardar", "Cancelar").
 */
@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    title: String,
    items: List<SettingItem>,
    config: SettingsDialogConfig,
    buttons: @Composable RowScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = config.shape,
            color = config.colors.containerColor,
            modifier = Modifier
                .widthIn(max = config.dimensions.dialogMaxWidth)
                .heightIn(max = config.dimensions.dialogMaxHeight)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // --- SECCIÓN DEL TÍTULO ---
                Column(modifier = Modifier.padding(config.dimensions.titlePadding)) {
                    Text(
                        text = title,
                        style = config.typography.titleTextStyle,
                        color = config.colors.titleContentColor
                    )
                }
                HorizontalDivider(color = config.colors.dividerColor)

                // --- SECCIÓN DE CONTENIDO (CON SCROLL) ---
                // Se itera sobre la lista de modelos y se renderiza el componente correcto.
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(config.dimensions.contentPadding)
                ) {
                    items.forEach { item ->
                        // El `when` nos asegura que manejamos todos los tipos definidos en SettingItem.
                        when (item) {
                            is ToggleSetting -> ToggleSettingRow(item)
                            is TextEntrySetting -> TextEntrySettingRow(item)
                            is ChoiceSetting -> ChoiceSettingRow(item)
                            is SliderSetting -> SliderSettingItem(item)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                HorizontalDivider(color = config.colors.dividerColor)

                // --- SECCIÓN DE BOTONES ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(config.dimensions.buttonsPadding),
                    horizontalArrangement = Arrangement.End,
                    content = buttons
                )
            }
        }
    }
}

// --- COMPONENTES DE UI PREDEFINIDOS PARA CADA MODELO ---

@Composable
private fun SettingRowContainer(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        content = content
    )
}

@Composable
private fun ToggleSettingRow(item: ToggleSetting) {
    SettingRowContainer {
        Text(item.title, modifier = Modifier.weight(1f))
        Switch(
            checked = item.state.value,
            onCheckedChange = { item.state.value = it }
        )
    }
}

@Composable
private fun TextEntrySettingRow(item: TextEntrySetting) {
    SettingRowContainer {
        Text(item.title, modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = item.state.value,
            onValueChange = { item.state.value = it },
            singleLine = true,
            modifier = Modifier.width(200.dp)
        )
    }
}

@Composable
private fun ChoiceSettingRow(item: ChoiceSetting) {
    SettingRowContainer {
        Text(item.title, modifier = Modifier.weight(1f))
        var expanded by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(item.state.value)
                Icon(EvaIcons.Outline.ArrowDown, contentDescription = "Desplegar")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                item.options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            item.state.value = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderSettingItem(item: SliderSetting) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("${item.title}: ${(item.state.value * 100).toInt()}%")
        Slider(
            value = item.state.value,
            onValueChange = { item.state.value = it },
            valueRange = item.valueRange,
            steps = item.steps
        )
    }
}

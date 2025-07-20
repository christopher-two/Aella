package org.christophertwo.aella.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Search
import compose.icons.evaicons.outline.Trash
import org.christophertwo.aella.utils.config.ProjectListConfig
import org.christophertwo.aella.utils.config.SettingItem.ChoiceSetting
import org.christophertwo.aella.utils.config.SettingItem.SliderSetting
import org.christophertwo.aella.utils.config.SettingItem.TextEntrySetting
import org.christophertwo.aella.utils.config.SettingItem.ToggleSetting
import org.christophertwo.aella.utils.config.SettingsDialogConfig.Companion.getDefaultSettingsDialogConfig
import org.christophertwo.aella.utils.model.ProjectData

/**
 * Muestra la información de un único proyecto en un formato de tarjeta.
 *
 * @param project El objeto [ProjectData] que se va a mostrar.
 * @param config La configuración visual [ProjectListConfig] para aplicar estilos.
 * @param modifier El [Modifier] para este composable.
 */
@Composable
private fun ProjectCard(
    project: ProjectData,
    config: ProjectListConfig,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = config.cardColors.containerColor,
            contentColor = config.cardColors.contentColor
        )
    ) {
        Column(modifier = Modifier.padding(config.paddings.cardPadding)) {
            // Fila superior: Título y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    style = config.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f) // Permite que el texto se ajuste si es largo
                )
                // "Badge" o etiqueta para el estado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50)) // Píldora
                        .background(project.status.color)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = project.status.displayName,
                        color = config.cardColors.statusBadgeTextColor,
                        style = config.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción del proyecto
            Text(
                text = project.description,
                style = config.typography.bodyMedium,
                color = config.cardColors.contentColor.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila inferior: Fecha y Miembros del equipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Creado: ${project.creationDate}",
                    style = config.typography.bodySmall,
                    color = config.cardColors.contentColor.copy(alpha = 0.6f)
                )
                Text(
                    text = "Equipo: ${project.teamMembers.joinToString(", ")}",
                    style = config.typography.bodySmall,
                    color = config.cardColors.contentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Un componente de pantalla completa que muestra una lista de proyectos con paginación y búsqueda.
 *
 * @param projects La lista actual de [ProjectData] para mostrar (ya filtrada si es necesario).
 * @param searchQuery El texto actual en la barra de búsqueda.
 * @param onSearchQueryChange La función que se invoca cuando el usuario escribe en la barra de búsqueda.
 * @param onLoadMore Una función lambda que se invoca para solicitar la carga de la siguiente página.
 * @param isLoadingMore Un booleano que indica si la carga de la siguiente página está en progreso.
 * @param hasMorePages Un booleano que indica si hay más páginas de proyectos para cargar.
 * @param config El objeto [ProjectListConfig] para personalizar la apariencia de la pantalla.
 * @param modifier El [Modifier] para este composable.
 */
@Composable
fun ProjectListScreen(
    projects: List<ProjectData>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    config: ProjectListConfig,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    // Creamos los estados mutables que nuestros modelos de configuración van a manipular.
    val notificationState = remember { mutableStateOf(true) }
    val usernameState = remember { mutableStateOf("ComposeFan") }
    val themeState = remember { mutableStateOf("Claro") }
    val volumeState = remember { mutableStateOf(0.75f) }

    // Definimos nuestra pantalla de configuración como una simple lista de datos.
    // El orden en esta lista es el orden en que aparecerán en la UI.
    val settingsList = remember {
        listOf(
            ToggleSetting("Habilitar notificaciones", notificationState),
            TextEntrySetting("Nombre de usuario", usernameState),
            ChoiceSetting(
                "Tema de la aplicación",
                themeState,
                listOf("Claro", "Oscuro", "Sistema"),
                {}
            ),
            SliderSetting("Volumen general", volumeState)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(config.backgroundColor)
            .padding(config.paddings.screenPadding)
    ) {
        // --- Barra de Búsqueda ---
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            placeholder = "Buscar proyectos por nombre o descripción...",
            config = config.searchBarConfig
        )

        Spacer(modifier = Modifier.height(config.paddings.searchBarBottomPadding))

        // --- Lista de Proyectos ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(config.paddings.spacingBetweenCards)
        ) {
            itemsIndexed(items = projects, key = { _, project -> project.id }) { index, project ->
                ProjectCard(
                    project = project,
                    config = config,
                    onClick = {
                        showDialog = true
                    }
                )

                val isLastItem = index == projects.lastIndex
                if (isLastItem && hasMorePages && !isLoadingMore) {
                    LaunchedEffect(Unit) {
                        onLoadMore()
                    }
                }
            }

            if (isLoadingMore) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        if (showDialog) {
            SettingsDialog(
                onDismissRequest = { showDialog = false },
                title = "Configuración de la Aplicación",
                items = settingsList, // Pasamos la lista de modelos directamente.
                config = getDefaultSettingsDialogConfig(),
                buttons = {
                    OutlinedButton(
                        onClick = { showDialog = false },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(onClick = {
                        // La lógica de guardado ahora solo necesita leer los estados.
                        println("Guardando: Notificaciones=${notificationState.value}, Usuario='${usernameState.value}', Tema='${themeState.value}', Volumen=${volumeState.value}")
                        showDialog = false
                    }) {
                        Text("Guardar")
                    }
                }
            )
        }
    }
}

/**
 * Una barra de búsqueda personalizable.
 *
 * @param query El texto actual de la búsqueda.
 * @param onQueryChange La función que se llama cuando el texto cambia.
 * @param placeholder El texto que se muestra cuando la barra está vacía.
 * @param config La configuración visual [ProjectListConfig.SearchBarConfig].
 * @param modifier El [Modifier] para este composable.
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    config: ProjectListConfig.SearchBarConfig,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .background(color = config.containerColor, shape = config.shape)
            .height(56.dp),
        textStyle = LocalTextStyle.current.copy(color = config.contentColor),
        cursorBrush = SolidColor(config.contentColor),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = EvaIcons.Outline.Search,
                    contentDescription = "Icono de búsqueda",
                    tint = config.contentColor
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(placeholder, color = config.placeholderColor)
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = EvaIcons.Outline.Trash,
                            contentDescription = "Limpiar búsqueda",
                            tint = config.contentColor
                        )
                    }
                }
            }
        }
    )
}

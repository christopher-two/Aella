package org.christophertwo.aella.utils.enums

import androidx.compose.ui.graphics.Color

/**
 * Representa el estado actual de un proyecto.
 * Incluye un nombre para mostrar y un color asociado para la UI.
 *
 * @property displayName El nombre legible del estado (ej. "En Progreso").
 * @property color El color que se usará para representar visualmente este estado.
 */
enum class ProjectStatus(val displayName: String, val color: Color) {
    IN_PROGRESS("En Progreso", Color(0xFF3B82F6)), // Azul
    COMPLETED("Completado", Color(0xFF22C55E)),   // Verde
    ON_HOLD("En Pausa", Color(0xFFF97316)),      // Naranja
    CANCELLED("Cancelado", Color(0xFFEF4444))     // Rojo
}
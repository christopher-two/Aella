package org.christophertwo.aella.data.room.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import org.christophertwo.aella.utils.enums.ProjectStatus

// --- TYPE CONVERTERS ---
// Room necesita saber cómo convertir tipos de datos complejos a tipos primitivos que pueda guardar.

/**
 * Contiene los conversores de tipos para la base de datos.
 * Room usará estos métodos automáticamente al leer o escribir datos.
 * Utiliza kotlinx.serialization para la conversión de JSON.
 */
class Converters {

    /**
     * Convierte un enum [ProjectStatus] a su representación en [String].
     * @param status El enum a convertir.
     * @return El nombre del enum como String.
     */
    @TypeConverter
    fun fromProjectStatus(status: ProjectStatus): String {
        return status.name
    }

    /**
     * Convierte un [String] de vuelta a un enum [ProjectStatus].
     * @param statusString El String a convertir.
     * @return El enum [ProjectStatus] correspondiente.
     */
    @TypeConverter
    fun toProjectStatus(statusString: String): ProjectStatus {
        return ProjectStatus.valueOf(statusString)
    }

    /**
     * Convierte una lista de Strings a un único String en formato JSON.
     * Usamos kotlinx.serialization para una serialización/deserialización robusta y multiplataforma.
     * @param teamMembers La lista de Strings.
     * @return Un String en formato JSON que representa la lista.
     */
    @TypeConverter
    fun fromStringList(teamMembers: List<String>): String {
        return Json.encodeToString(teamMembers)
    }

    /**
     * Convierte un String en formato JSON de vuelta a una lista de Strings.
     * @param jsonString El String en formato JSON.
     * @return La lista de Strings reconstruida.
     */
    @TypeConverter
    fun toStringList(jsonString: String): List<String> {
        return Json.decodeFromString<List<String>>(jsonString)
    }
}

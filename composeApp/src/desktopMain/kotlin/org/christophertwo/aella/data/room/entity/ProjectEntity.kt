package org.christophertwo.aella.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.christophertwo.aella.utils.enums.ProjectStatus

/**
 * Representa la tabla 'projects' en la base de datos.
 * Esta clase está diseñada para ser almacenada por Room y es la contraparte
 * de la clase `ProjectData` que se usa en la capa de UI/Dominio.
 *
 * @property id El identificador único del proyecto. Es la clave primaria.
 * @property name El nombre del proyecto.
 * @property description Una descripción detallada del proyecto.
 * @property status El estado actual del proyecto (convertido a String para almacenamiento).
 * @property creationDate La fecha de creación del proyecto.
 * @property teamMembersJson Una lista de IDs o nombres de miembros del equipo, almacenada como un String en formato JSON.
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val status: ProjectStatus,
    val creationDate: String,
    val teamMembersJson: String // Almacenamos la lista como un JSON String
)

package org.christophertwo.aella.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la tabla 'workers' en la base de datos.
 * Se incluye para demostrar la escalabilidad del sistema.
 *
 * @property id El identificador único del trabajador.
 * @property name El nombre completo del trabajador.
 * @property role El puesto o rol del trabajador en la empresa.
 * @property email El correo electrónico de contacto del trabajador.
 */
@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: String,
    val email: String
)
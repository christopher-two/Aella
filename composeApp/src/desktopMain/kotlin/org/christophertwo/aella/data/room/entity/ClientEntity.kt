package org.christophertwo.aella.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la tabla 'clients' en la base de datos.
 * Se incluye para demostrar la escalabilidad del sistema.
 *
 * @property id El identificador único del cliente.
 * @property name El nombre completo del cliente.
 * @property email El correo electrónico de contacto del cliente.
 * @property phone El número de teléfono del cliente.
 */
@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String
)

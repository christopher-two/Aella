package org.christophertwo.aella.utils.model

/**
 * Contiene los datos para representar un cliente en la capa de UI.
 * Es una clase de datos inmutable para garantizar la consistencia.
 *
 * @property id El identificador único del cliente.
 * @property name El nombre completo del cliente.
 * @property email El correo electrónico de contacto del cliente.
 * @property phone El número de teléfono de contacto del cliente.
 */
data class ClientData(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = ""
)

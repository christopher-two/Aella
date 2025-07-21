package org.christophertwo.aella.utils.model

/**
 * Contiene los datos para representar un trabajador en la capa de UI.
 * Es una clase de datos inmutable.
 *
 * @property id El identificador único del trabajador.
 * @property name El nombre completo del trabajador.
 * @property role El puesto o rol del trabajador dentro de la empresa.
 * @property email El correo electrónico de contacto del trabajador.
 */
data class WorkerData(
    val id: String = "",
    val name: String = "",
    val role: String = "",
    val email: String = ""
)

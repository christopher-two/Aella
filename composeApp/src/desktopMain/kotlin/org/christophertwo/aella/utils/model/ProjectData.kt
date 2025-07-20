package org.christophertwo.aella.utils.model

import org.christophertwo.aella.utils.enums.ProjectStatus

/**
 * Contiene todos los datos necesarios para representar un proyecto.
 * Es inmutable para garantizar la consistencia de los datos en la UI.
 *
 * @property id Un identificador único para el proyecto.
 * @property name El nombre o título del proyecto.
 * @property description Un resumen corto de lo que trata el proyecto.
 * @property status El estado actual del proyecto, usando el enum [org.christophertwo.aella.utils.enums.ProjectStatus].
 * @property creationDate La fecha de creación como texto formateado.
 * @property teamMembers Una lista de nombres de los miembros del equipo.
 */
data class ProjectData(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: ProjectStatus = ProjectStatus.COMPLETED,
    val creationDate: String = "",
    val teamMembers: List<String> = emptyList()
) {
    companion object {
        fun generateDummyProjects(page: Int, pageSize: Int): List<ProjectData> {
            val startIndex = (page - 1) * pageSize
            return (startIndex until startIndex + pageSize).map { i ->
                ProjectData(
                    id = "proj_$i",
                    name = "Proyecto Alpha ${i + 1}",
                    description = "Esta es la descripción del proyecto número ${i + 1}. El objetivo es desarrollar una nueva funcionalidad clave.",
                    status = ProjectStatus.entries.toTypedArray().random(),
                    creationDate = "1${i % 9 + 1}/07/2025",
                    teamMembers = listOf("Ana", "Luis", "Eva")
                )
            }
        }
    }
}

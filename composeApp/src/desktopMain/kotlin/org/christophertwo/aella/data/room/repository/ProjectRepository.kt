package org.christophertwo.aella.data.room.repository

import kotlinx.coroutines.flow.Flow
import org.christophertwo.aella.utils.model.ProjectData

/**
 * Interfaz para el repositorio de proyectos.
 * Define el contrato que la implementación debe seguir.
 */
interface ProjectRepository {
    suspend fun getProjects(page: Int, pageSize: Int): List<ProjectData>
    fun getProject(id: String): Flow<ProjectData?>
    suspend fun addProject(project: ProjectData)
    suspend fun updateProject(project: ProjectData)
    suspend fun deleteProject(project: ProjectData)
    suspend fun searchProjects(query: String, page: Int, pageSize: Int): List<ProjectData>
}
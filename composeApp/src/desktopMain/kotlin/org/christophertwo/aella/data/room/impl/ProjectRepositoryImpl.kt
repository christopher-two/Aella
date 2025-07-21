package org.christophertwo.aella.data.room.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.christophertwo.aella.data.room.dao.ProjectDao
import org.christophertwo.aella.data.room.entity.ProjectEntity
import org.christophertwo.aella.data.room.repository.ProjectRepository
import org.christophertwo.aella.utils.model.ProjectData

/**
 * Implementación del repositorio de proyectos que utiliza Room como fuente de datos.
 */
class ProjectRepositoryImpl(
    private val projectDao: ProjectDao
) : ProjectRepository {

    override suspend fun getProjects(page: Int, pageSize: Int): List<ProjectData> {
        return withContext(Dispatchers.IO) {
            val offset = (page - 1) * pageSize
            projectDao.getAllProjects(limit = pageSize, offset = offset).map { it.toProjectData() }
        }
    }


    override fun getProject(id: String): Flow<ProjectData?> {
        return projectDao.getProjectById(id).map { entity ->
            entity?.toProjectData()
        }
    }

    override suspend fun addProject(project: ProjectData) {
        withContext(Dispatchers.IO) {
            projectDao.insertOrUpdate(project.toProjectEntity())
        }
    }

    override suspend fun updateProject(project: ProjectData) {
        withContext(Dispatchers.IO) {
            projectDao.insertOrUpdate(project.toProjectEntity())
        }
    }

    override suspend fun deleteProject(project: ProjectData) {
        withContext(Dispatchers.IO) {
            projectDao.delete(project.toProjectEntity())
        }
    }

    override suspend fun searchProjects(
        query: String,
        page: Int,
        pageSize: Int
    ): List<ProjectData> {
        return withContext(Dispatchers.IO) {
            val offset = (page - 1) * pageSize
            val searchQuery = "%$query%"
            projectDao.searchProjects(searchQuery, limit = pageSize, offset = offset)
                .map { it.toProjectData() }
        }
    }

    private fun ProjectEntity.toProjectData(): ProjectData {
        val teamMembersList = Json.decodeFromString<List<String>>(this.teamMembersJson)
        return ProjectData(
            id = this.id,
            name = this.name,
            description = this.description,
            status = this.status,
            creationDate = this.creationDate,
            teamMembers = teamMembersList
        )
    }

    private fun ProjectData.toProjectEntity(): ProjectEntity {
        val teamMembersJson = Json.encodeToString(this.teamMembers)
        return ProjectEntity(
            id = this.id,
            name = this.name,
            description = this.description,
            status = this.status,
            creationDate = this.creationDate,
            teamMembersJson = teamMembersJson
        )
    }
}
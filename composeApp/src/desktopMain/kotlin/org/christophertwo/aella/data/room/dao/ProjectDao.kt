package org.christophertwo.aella.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.christophertwo.aella.data.room.entity.ProjectEntity

/**
 * DAO para la entidad Project.
 * Define los métodos de acceso a la base de datos para la tabla 'projects'.
 * El uso de Flow permite que la UI observe los cambios en tiempo real.
 */
@Dao
interface ProjectDao {

    /**
     * Inserta un proyecto nuevo o actualiza uno existente si la clave primaria ya existe.
     * La anotación @Upsert es una forma conveniente de manejar inserciones y actualizaciones.
     * @param project La entidad del proyecto a guardar.
     */
    @Upsert
    suspend fun insertOrUpdate(project: ProjectEntity)

    /**
     * Elimina un proyecto de la base de datos.
     * @param project La entidad del proyecto a eliminar.
     */
    @Delete
    suspend fun delete(project: ProjectEntity)

    /**
     * Obtiene un único proyecto por su ID.
     * @param id El ID del proyecto a buscar.
     * @return Un Flow que emite la entidad del proyecto, o null si no se encuentra.
     */
    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: String): Flow<ProjectEntity?>

    /**
     * Obtiene todos los proyectos de la base de datos, ordenados por fecha de creación.
     * @return Un Flow que emite la lista completa de proyectos.
     */
    @Query("SELECT * FROM projects ORDER BY creationDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getAllProjects(limit: Int, offset: Int): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE name LIKE :query OR description LIKE :query ORDER BY creationDate DESC LIMIT :limit OFFSET :offset")
    suspend fun searchProjects(query: String, limit: Int, offset: Int): List<ProjectEntity>
}


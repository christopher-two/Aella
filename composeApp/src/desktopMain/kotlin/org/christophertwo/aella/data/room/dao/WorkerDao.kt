package org.christophertwo.aella.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.christophertwo.aella.data.room.entity.WorkerEntity

/**
 * DAO para la entidad Worker.
 * Demuestra cómo se añadirían otros DAOs al sistema.
 */
@Dao
interface WorkerDao {
    @Upsert
    suspend fun insertOrUpdate(worker: WorkerEntity)

    @Query("SELECT * FROM workers ORDER BY name ASC")
    fun getAllWorkers(): Flow<List<WorkerEntity>>
}

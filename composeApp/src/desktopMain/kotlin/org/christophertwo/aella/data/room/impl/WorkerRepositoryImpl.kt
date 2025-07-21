package org.christophertwo.aella.data.room.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.christophertwo.aella.data.room.dao.WorkerDao
import org.christophertwo.aella.data.room.entity.WorkerEntity
import org.christophertwo.aella.data.room.repository.WorkerRepository
import org.christophertwo.aella.utils.model.WorkerData

/**
 * Implementación del repositorio de trabajadores que usa Room.
 */
class WorkerRepositoryImpl(private val workerDao: WorkerDao) : WorkerRepository {
    override fun getWorkers(): Flow<List<WorkerData>> {
        return workerDao.getAllWorkers().map { entities ->
            entities.map { it.toWorkerData() }
        }
    }

    override suspend fun addWorker(worker: WorkerData) {
        withContext(Dispatchers.IO) {
            workerDao.insertOrUpdate(worker.toWorkerEntity())
        }
    }

    private fun WorkerEntity.toWorkerData(): WorkerData = WorkerData(id, name, role, email)
    private fun WorkerData.toWorkerEntity(): WorkerEntity = WorkerEntity(id, name, role, email)
}

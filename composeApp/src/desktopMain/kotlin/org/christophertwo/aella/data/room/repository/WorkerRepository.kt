package org.christophertwo.aella.data.room.repository

import kotlinx.coroutines.flow.Flow
import org.christophertwo.aella.utils.model.WorkerData

/**
 * Interfaz para el repositorio de trabajadores. Define el contrato para el acceso a datos de trabajadores.
 */
interface WorkerRepository {
    fun getWorkers(): Flow<List<WorkerData>>
    suspend fun addWorker(worker: WorkerData)
}

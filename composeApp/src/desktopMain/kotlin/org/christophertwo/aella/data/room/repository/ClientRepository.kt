package org.christophertwo.aella.data.room.repository

import kotlinx.coroutines.flow.Flow
import org.christophertwo.aella.utils.model.ClientData

/**
 * Interfaz para el repositorio de clientes. Define el contrato para el acceso a datos de clientes.
 */
interface ClientRepository {
    fun getClients(): Flow<List<ClientData>>
    suspend fun addClient(client: ClientData)
}
package org.christophertwo.aella.data.room.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.christophertwo.aella.data.room.dao.ClientDao
import org.christophertwo.aella.data.room.entity.ClientEntity
import org.christophertwo.aella.data.room.repository.ClientRepository
import org.christophertwo.aella.utils.model.ClientData

/**
 * Implementación del repositorio de clientes que usa Room.
 */
class ClientRepositoryImpl(private val clientDao: ClientDao) : ClientRepository {
    override fun getClients(): Flow<List<ClientData>> {
        return clientDao.getAllClients().map { entities ->
            entities.map { it.toClientData() }
        }
    }

    override suspend fun addClient(client: ClientData) {
        withContext(Dispatchers.IO) {
            clientDao.insertOrUpdate(client.toClientEntity())
        }
    }

    private fun ClientEntity.toClientData(): ClientData = ClientData(id, name, email, phone)
    private fun ClientData.toClientEntity(): ClientEntity = ClientEntity(id, name, email, phone)
}
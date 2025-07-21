package org.christophertwo.aella.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.christophertwo.aella.data.room.entity.ClientEntity

/**
 * DAO para la entidad Client.
 * Demuestra cómo se añadirían otros DAOs al sistema.
 */
@Dao
interface ClientDao {
    @Upsert
    suspend fun insertOrUpdate(client: ClientEntity)

    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): Flow<List<ClientEntity>>
}
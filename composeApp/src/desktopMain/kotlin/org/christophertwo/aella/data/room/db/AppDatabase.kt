package org.christophertwo.aella.data.room.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.christophertwo.aella.data.room.converters.Converters
import org.christophertwo.aella.data.room.dao.ClientDao
import org.christophertwo.aella.data.room.dao.ProjectDao
import org.christophertwo.aella.data.room.dao.WorkerDao
import org.christophertwo.aella.data.room.entity.ClientEntity
import org.christophertwo.aella.data.room.entity.ProjectEntity
import org.christophertwo.aella.data.room.entity.WorkerEntity
import java.io.File

/**
 * La clase principal de la base de datos de la aplicación.
 *
 * @property entities Un array de todas las clases de entidad que la base de datos manejará.
 * @property version La versión de la base de datos. Debe incrementarse al cambiar el esquema.
 * @property exportSchema Si se debe exportar el esquema a un archivo JSON. Útil para migraciones.
 */
@Database(
    entities = [ProjectEntity::class, ClientEntity::class, WorkerEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Métodos abstractos para que Room provea la implementación de cada DAO.
    abstract fun projectDao(): ProjectDao
    abstract fun clientDao(): ClientDao
    abstract fun workerDao(): WorkerDao

    companion object {

        private const val DATABASE_NAME = "aella_app.db"

        fun getRoomDatabase(
            builder: Builder<AppDatabase>
        ): AppDatabase {
            return builder
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }

        fun buildDatabase(): Builder<AppDatabase> {
            val dbFile = File(System.getProperty("java.io.tmpdir"), DATABASE_NAME)
            return Room.databaseBuilder<AppDatabase>(
                name = dbFile.absolutePath,
            )
        }
    }
}
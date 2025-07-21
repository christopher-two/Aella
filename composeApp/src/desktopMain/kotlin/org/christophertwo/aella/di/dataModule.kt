package org.christophertwo.aella.di

import org.christophertwo.aella.data.room.db.AppDatabase
import org.christophertwo.aella.data.room.impl.ClientRepositoryImpl
import org.christophertwo.aella.data.room.impl.ProjectRepositoryImpl
import org.christophertwo.aella.data.room.impl.WorkerRepositoryImpl
import org.christophertwo.aella.data.room.repository.ClientRepository
import org.christophertwo.aella.data.room.repository.ProjectRepository
import org.christophertwo.aella.data.room.repository.WorkerRepository
import org.koin.dsl.module

/**
 * Módulo de Koin para la inyección de dependencias de la capa de datos.
 */
val dataModule = module {

    single { AppDatabase.buildDatabase() }
    single { AppDatabase.getRoomDatabase(get()) }

    // Se proveen los DAOs como singletons, obteniéndolos de la instancia de la BD.
    single { get<AppDatabase>().projectDao() }
    single { get<AppDatabase>().clientDao() }
    single { get<AppDatabase>().workerDao() }

    // Se proveen las implementaciones de los repositorios como singletons.
    // Cuando se pida un `ProjectRepository`, Koin sabrá que debe usar `ProjectRepositoryImpl`.
    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<ClientRepository> { ClientRepositoryImpl(get()) }
    single<WorkerRepository> { WorkerRepositoryImpl(get()) }
}

package org.getscol.gscol.core.di

import org.getscol.gscol.core.data.storage.DataStoreLocalStorage
import org.getscol.gscol.core.data.storage.LocalStorage
import org.getscol.gscol.core.data.storage.createPlatformDataStore
import org.koin.dsl.module

val storageModule = module {
    single { createPlatformDataStore() }
    single<LocalStorage> { DataStoreLocalStorage(get()) }
}
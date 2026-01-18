package org.getscol.gscol.core.di

import org.getscol.gscol.feature.auth.di.authModule
import org.koin.core.module.Module

expect val platformModule: Module

fun appModules() = listOf(
    platformModule,
    networkModule,
    storageModule,
    authModule
)



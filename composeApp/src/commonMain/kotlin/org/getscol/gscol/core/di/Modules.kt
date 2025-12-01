package org.getscol.gscol.core.di

import org.koin.core.module.Module

expect val platformModule: Module

fun appModules() = listOf(
    platformModule,
    networkModule,
    storageModule
)



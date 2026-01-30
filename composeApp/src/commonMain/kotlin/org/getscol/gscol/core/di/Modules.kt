package org.getscol.gscol.core.di

import org.getscol.gscol.auth.di.authModule
import org.getscol.gscol.feature.home.di.homeModule
import org.koin.core.module.Module

expect val platformModule: Module

fun appModules() = listOf(
    platformModule,
    networkModule,
    sessionModule,
    storageModule,
    authModule,
    homeModule
)



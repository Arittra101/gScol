package org.getscol.gscol.core.di

import org.getscol.gscol.auth.di.authModule
import org.getscol.gscol.feature.academic_form.di.academicModule
import org.getscol.gscol.feature.home.di.homeModule
import org.getscol.gscol.feature.search.di.searchModule
import org.koin.core.module.Module

expect val platformModule: Module

fun appModules() = listOf(
    platformModule,
    networkModule,
    storageModule,
    authModule,
    sessionModule,
    homeModule,
    searchModule,
    academicModule
)



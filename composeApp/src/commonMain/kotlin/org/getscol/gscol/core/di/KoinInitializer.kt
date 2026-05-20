package org.getscol.gscol.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(chuckerModule: Module, config: KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
        modules(appModules(chuckerModule))
    }
}
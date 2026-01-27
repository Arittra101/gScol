package org.getscol.gscol.home.di

import org.getscol.gscol.home.presentation.Homeviewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val homeModule = module {
    viewModelOf(::Homeviewmodel)
}
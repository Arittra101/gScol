package org.getscol.gscol.feature.profile.di

import org.getscol.gscol.feature.profile.presentation.ProfileViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val profileModule = module {
    viewModelOf(::ProfileViewmodel)
}
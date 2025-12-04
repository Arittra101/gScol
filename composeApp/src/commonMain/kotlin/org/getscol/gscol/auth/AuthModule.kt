package org.getscol.gscol.auth

import org.getscol.gscol.auth.presentation.AuthViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {

    viewModelOf(::AuthViewmodel)


}
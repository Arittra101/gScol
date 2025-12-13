package org.getscol.gscol.auth.di

import org.getscol.gscol.auth.data.AuthTokenProvider
import org.getscol.gscol.auth.data.api_service.AuthApiService
import org.getscol.gscol.auth.domain.repository.AuthRepository
import org.getscol.gscol.auth.data.repository.AuthRepositoryImpl
import org.getscol.gscol.auth.data.api_service.KtorAuthApiService
import org.getscol.gscol.auth.presentation.login.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {

    singleOf(::AuthTokenProvider)
    // API Service
    singleOf(::KtorAuthApiService).bind<AuthApiService>()
    
    // Repository
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    
    // ViewModels
    viewModelOf(::LoginViewModel)
}

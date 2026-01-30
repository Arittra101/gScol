package org.getscol.gscol.auth.di

import org.getscol.gscol.feature.auth.data.AuthTokenProvider
import org.getscol.gscol.feature.auth.data.api_service.AuthApiService
import org.getscol.gscol.feature.auth.data.api_service.AuthApiServiceImpl
import org.getscol.gscol.feature.auth.data.repository.AuthRepositoryImpl
import org.getscol.gscol.feature.auth.domain.repository.AuthRepository
import org.getscol.gscol.feature.auth.presentation.forgotpassword.ForgotPasswordViewModel
import org.getscol.gscol.feature.auth.presentation.login.LoginViewModel
import org.getscol.gscol.feature.auth.presentation.otp.OtpVerificationViewModel
import org.getscol.gscol.feature.auth.presentation.registration.RegistrationViewModel
import org.getscol.gscol.feature.auth.presentation.resetpassword.ResetPasswordViewmodel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {

    singleOf(::AuthTokenProvider)
    // API Service
    singleOf(::AuthApiServiceImpl).bind<AuthApiService>()
    
    // Repository
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    
    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::OtpVerificationViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewmodel)
}

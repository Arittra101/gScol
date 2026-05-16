package org.getscol.gscol.feature.profile.di

import org.getscol.gscol.feature.profile.data.api_service.DocumentDownloadApiService
import org.getscol.gscol.feature.profile.data.api_service.DocumentDownloadApiServiceImpl
import org.getscol.gscol.feature.profile.data.api_service.EditProfileApiService
import org.getscol.gscol.feature.profile.data.api_service.EditProfileApiServiceImpl
import org.getscol.gscol.feature.profile.data.repository.EditProfileRepositoryImpl
import org.getscol.gscol.feature.profile.domain.repository.EditProfileRepository
import org.getscol.gscol.feature.profile.presentation.ProfileViewmodel
import org.getscol.gscol.feature.profile.presentation.edit_profile.EditProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val profileModule = module {
    viewModelOf(::ProfileViewmodel)
    viewModelOf(::EditProfileViewModel)

    single<EditProfileApiService> { EditProfileApiServiceImpl(get()) }
    single<DocumentDownloadApiService> { DocumentDownloadApiServiceImpl(get()) }
    single<EditProfileRepository> { EditProfileRepositoryImpl(get(), get()) }
}

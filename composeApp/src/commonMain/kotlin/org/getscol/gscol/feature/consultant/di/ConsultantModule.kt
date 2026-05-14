package org.getscol.gscol.feature.consultant.di

import org.getscol.gscol.feature.consultant.data.api_service.ConsultantApiService
import org.getscol.gscol.feature.consultant.data.api_service.ConsultantApiServiceImpl
import org.getscol.gscol.feature.consultant.data.repository.ConsultantRepositoryImpl
import org.getscol.gscol.feature.consultant.domain.repository.ConsultantRepository
import org.getscol.gscol.feature.consultant.presentation.consultant.ConsultantViewModel
import org.getscol.gscol.feature.consultant.presentation.details.ConsultantDetailsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val consultantModule = module {
    singleOf(::ConsultantApiServiceImpl).bind<ConsultantApiService>()
    singleOf(::ConsultantRepositoryImpl).bind<ConsultantRepository>()

    viewModelOf(::ConsultantViewModel)
    viewModel { params ->
        ConsultantDetailsViewModel(
            consultantId = params.get(),
            repository = get(),
        )
    }
}
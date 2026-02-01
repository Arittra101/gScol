package org.getscol.gscol.home.di

import org.getscol.gscol.home.data.api_service.HomeApiService
import org.getscol.gscol.home.data.api_service.HomeApiServiceImpl
import org.getscol.gscol.home.data.repository.HomeRepositoryImpl
import org.getscol.gscol.home.domain.repository.HomeRepository
import org.getscol.gscol.home.presentation.HomeViewmodel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


val homeModule = module {
//    singleOf(::HomeApiServiceImpl).bind<HomeApiService>()
    single<HomeApiService> { HomeApiServiceImpl(get(named("mock"))) }
    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    viewModelOf(::HomeViewmodel)
}
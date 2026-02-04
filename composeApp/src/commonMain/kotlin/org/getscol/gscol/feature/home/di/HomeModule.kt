package org.getscol.gscol.feature.home.di

import org.getscol.gscol.feature.home.data.api_service.HomeApiService
import org.getscol.gscol.feature.home.data.api_service.HomeApiServiceImpl
import org.getscol.gscol.feature.home.data.repository.HomeRepositoryImpl
import org.getscol.gscol.feature.home.domain.repository.HomeRepository
import org.getscol.gscol.feature.home.presentation.HomeViewmodel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


val homeModule = module {

 /*   single<HomeApiService> { HomeApiServiceImpl(get(named("mock"))) }  for mock client*/

    singleOf(::HomeApiServiceImpl).bind<HomeApiService>()
    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    viewModelOf(::HomeViewmodel)
}
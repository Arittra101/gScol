package org.getscol.gscol.feature.search.di

import org.getscol.gscol.feature.search.data.api_service.SearchApiService
import org.getscol.gscol.feature.search.data.api_service.SearchApiServiceImpl
import org.getscol.gscol.feature.search.data.repository.SearchRepositoryImpl
import org.getscol.gscol.feature.search.domain.repository.SearchRepository
import org.getscol.gscol.feature.search.presentation.advance_search.AdvancedSearchViewModel
import org.getscol.gscol.feature.search.presentation.search_result.SearchResultsViewModel
import org.getscol.gscol.feature.search.presentation.search.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {
    singleOf(::SearchApiServiceImpl).bind<SearchApiService>()
    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()
    viewModelOf(::SearchViewModel)
    viewModelOf(::AdvancedSearchViewModel)
    viewModelOf(::SearchResultsViewModel)
}

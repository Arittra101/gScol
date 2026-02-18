package org.getscol.gscol.feature.search.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.data.storage.LocalStorage
import org.getscol.gscol.core.data.storage.StorageKeys
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.search.data.api_service.SearchApiService
import org.getscol.gscol.feature.search.data.paging.SearchPagingSource
import org.getscol.gscol.feature.search.domain.model.AdvancedFilterOptions
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.domain.model.FilterOption
import org.getscol.gscol.feature.search.domain.repository.SearchRepository

private const val MAX_SEARCH_HISTORY = 50

class SearchRepositoryImpl(
    private val searchApiService: SearchApiService,
    private val localStorage: LocalStorage
) : SearchRepository {

    override fun getSearchResultsStream(
        searchText: String,
        listType: String,
        isLoggedIn: Boolean,
        advancedParams: AdvancedSearchParams?
    ): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    searchApiService = searchApiService,
                    searchText = searchText,
                    listType = listType,
                    isUserLogin = isLoggedIn,
                    advancedParams = advancedParams
                )
            }
        ).flow
    }

    override suspend fun getSearchHistory(): List<String> {
        val raw = localStorage.getString(StorageKeys.SEARCH_HISTORY) ?: return emptyList()
        return try {
            Json.decodeFromString<List<String>>(raw).distinct()
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun addToSearchHistory(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        val current = getSearchHistory()
        val updated = listOf(trimmed) + current.filter { it.equals(trimmed, ignoreCase = true).not() }
            .take(MAX_SEARCH_HISTORY - 1)
        localStorage.setString(StorageKeys.SEARCH_HISTORY, Json.encodeToString(updated))
    }

    override suspend fun removeFromSearchHistory(query: String) {
        val current = getSearchHistory().filter { it != query }
        localStorage.setString(StorageKeys.SEARCH_HISTORY, Json.encodeToString(current))
    }

    override suspend fun getAdvancedFilterOptions(): Result<AdvancedFilterOptions, org.getscol.gscol.core.domain.DataError.Remote> {
        return when (val result = searchApiService.getAdvancedFilters()) {
            is Result.Success -> {
                val filters = result.data.data?.filters.orEmpty()
                val countryOptions = filters.find { it.name == "country" }?.values?.map { FilterOption(it.id, it.name) }.orEmpty()
                val programmeOptions = filters.find { it.name == "programme" }?.values?.map { FilterOption(it.id, it.name) }.orEmpty()
                Result.Success(AdvancedFilterOptions(countryOptions = countryOptions, programmeOptions = programmeOptions))
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getCities(countryId: String): Result<List<FilterOption>, org.getscol.gscol.core.domain.DataError.Remote> {
        return when (val result = searchApiService.getCities(countryId)) {
            is Result.Success -> {
                val cities = result.data.data?.cities?.map { FilterOption(it.id, it.name) }.orEmpty()
                Result.Success(cities)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}

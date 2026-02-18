package org.getscol.gscol.feature.search.domain.repository

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.search.domain.model.AdvancedFilterOptions
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.domain.model.FilterOption

interface SearchRepository {
    fun getSearchResultsStream(
        searchText: String,
        listType: String,
        isLoggedIn: Boolean,
        advancedParams: AdvancedSearchParams? = null
    ): Flow<PagingData<Course>>
    suspend fun getSearchHistory(): List<String>
    suspend fun addToSearchHistory(query: String)
    suspend fun removeFromSearchHistory(query: String)
    suspend fun getAdvancedFilterOptions(): Result<AdvancedFilterOptions, DataError.Remote>
    suspend fun getCities(countryId: String): Result<List<FilterOption>, DataError.Remote>
}

package org.getscol.gscol.feature.search.domain.repository

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams

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
}

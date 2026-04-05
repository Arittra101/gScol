package org.getscol.gscol.feature.search.presentation.search_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import app.cash.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.domain.repository.SearchRepository
import kotlin.collections.plus

data class SearchResultsParams(
    val searchText: String,
    val listType: String,
    val advancedParams: AdvancedSearchParams?
)

@OptIn(ExperimentalCoroutinesApi::class)
class SearchResultsViewModel(
    private val searchRepository: SearchRepository,
    private val session: Session
) : ViewModel() {

    private val params = MutableStateFlow<SearchResultsParams?>(null)
    private val favoriteUpdates = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    fun setParams(searchText: String, listType: String, advancedParams: AdvancedSearchParams?) {
        params.update {
            if (it?.searchText == searchText && it.listType == listType && it.advancedParams == advancedParams) it
            else SearchResultsParams(searchText, listType, advancedParams)
        }
    }


    val baseCourses: Flow<PagingData<Course>> = params.flatMapLatest { p ->
                if (p == null) flow<PagingData<Course>> { }
                else session.isUserLoggedIn
                    .distinctUntilChanged()
                    .flatMapLatest { isLoggedIn ->
                        searchRepository.getSearchResultsStream(
                            searchText = p.searchText,
                            listType = p.listType,
                            isLoggedIn = isLoggedIn,
                            advancedParams = p.advancedParams
                        )
                    }
            }.cachedIn(viewModelScope)

    val courses: Flow<PagingData<Course>> =
        combine(baseCourses, favoriteUpdates) { pagingData, favourite ->
            pagingData.map { course ->
                favourite[course.courseId]?.let { newValue ->
                    course.copy(isWishlisted = newValue)
                } ?: course
            }
        }

    fun onAction(action: HomeAction) {
        when(action){
            is HomeAction.AddToWishlist -> favoriteUpdates.update { current -> current + ((action.courseId to !action.isWishListed)) }
        }
    }
}

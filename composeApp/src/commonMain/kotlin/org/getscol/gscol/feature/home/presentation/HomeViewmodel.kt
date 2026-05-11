package org.getscol.gscol.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import app.cash.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.domain.repository.HomeRepository
import org.getscol.gscol.feature.wishlist.WishlistMutationUiState
import org.getscol.gscol.feature.wishlist.domain.repository.WishlistRepository

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewmodel(
    private val homeRepository: HomeRepository,
    private val wishlistRepository: WishlistRepository,
    val session: Session,
) :
    ViewModel() {

    private val wishlistMutationMutex = Mutex()
    private val _wishlistMutationUiState = MutableStateFlow(WishlistMutationUiState())
    val wishlistMutationUiState = _wishlistMutationUiState.asStateFlow()

    private val favoriteUpdates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val baseCourses: Flow<PagingData<Course>> =
        combine(session.isUserLoggedIn, session.academicFormSubmitTrigger) { isLoggedIn, _ ->
            isLoggedIn
        }.flatMapLatest { isLoggedIn ->
            /* favoriteUpdates.value = emptyMap()*/
            homeRepository.getHomeCoursesStream(isLoggedIn)
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
        when (action) {
            is HomeAction.AddToWishlist -> {
                viewModelScope.launch {
                    if (!session.isUserLoggedIn.first()) return@launch
                    wishlistMutationMutex.withLock {
                        _wishlistMutationUiState.update { it.copy(isMutating = true) }
                        try {
                            if (action.isWishListed) {
                                when (wishlistRepository.removeFromWishlist(action.courseId)) {
                                    is Result.Success -> favoriteUpdates.update { it + (action.courseId to false) }
                                    is Result.Error -> Unit
                                }
                            } else {
                                when (wishlistRepository.addToWishlist(action.courseId)) {
                                    is Result.Success -> favoriteUpdates.update { it + (action.courseId to true) }
                                    is Result.Error -> Unit
                                }
                            }
                        } finally {
                            _wishlistMutationUiState.update { it.copy(isMutating = false) }
                        }
                    }
                }
            }
        }
    }

}

sealed interface HomeAction {
    data class AddToWishlist(val courseId: String, val isWishListed: Boolean) : HomeAction
}


/*
Soon we will remove this shit and move on to our own pagination wrapper :)
2️⃣ What “lazy” means here

Lazy = the transformation is applied only when each item is loaded and displayed.

Timeline example:

Time 0: Page 1 loaded with items 1-10
PagingData.map { ... } runs for 1-10 → UI shows items
Time 1: User scrolls → Page 2 loaded with items 11-20
PagingData.map { ... } runs for 11-20 → UI shows new items
Page 2 items are transformed only when they arrive
Page 1 items were already transformed earlier
You don’t need to recompute everything for every new page

That’s why it’s called lazy transformation — done just in time for each page.
*/
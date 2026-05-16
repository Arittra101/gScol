package org.getscol.gscol.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
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
import kotlin.collections.plus

@OptIn(ExperimentalCoroutinesApi::class)
class InEligibleCourseViewmodel(
    private val homeRepository: HomeRepository,
    private val wishlistRepository: WishlistRepository,
    val session: Session,
) :
    ViewModel() {

    private val wishlistMutationMutex = Mutex()
    private val _wishlistMutationUiState = MutableStateFlow(WishlistMutationUiState())
    val wishlistMutationUiState = _wishlistMutationUiState.asStateFlow()

    private val favoriteUpdates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val triggerApiCall = MutableStateFlow(Unit)

    companion object {
        const val INELIGIBLE_ONLY = "INELIGIBLE_ONLY"
    }

    init {
        triggerApiCall.value = Unit
    }

    private val baseCourses = triggerApiCall.flatMapLatest {
        val isLogin = session.isUserLoggedIn.value
        homeRepository.getHomeCoursesStream(isLogin, INELIGIBLE_ONLY)
    }.cachedIn(viewModelScope)

    val courses: Flow<PagingData<Course>> =
        combine(baseCourses, favoriteUpdates) { pagingData, favourite ->
            pagingData.map { course ->
                favourite[course.courseId]?.let { newValue ->
                    course.copy(isWishListed = newValue)
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


package org.getscol.gscol.feature.compare.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.wishlist.asWishlistUiMessage
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.feature.wishlist.domain.repository.WishlistRepository

sealed interface CompareUiState {
    data object Loading : CompareUiState
    data class Error(val message: String) : CompareUiState
    data class Content(
        val courses: List<Course>,
        val isWishlistMutating: Boolean = false,
    ) : CompareUiState
}

class CompareViewModel(
    private val wishlistRepository: WishlistRepository,
    private val session: Session,
) : ViewModel() {

    private val wishlistMutationMutex = Mutex()

    private val _uiState = MutableStateFlow<CompareUiState>(CompareUiState.Loading)
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()

    init {
        loadWishlists()
    }

    fun loadWishlists() {
        viewModelScope.launch {
            _uiState.value = CompareUiState.Loading
            when (val r = wishlistRepository.getWishlists()) {
                is Result.Success -> _uiState.value = CompareUiState.Content(r.data)
                is Result.Error -> _uiState.value = CompareUiState.Error(r.error.asWishlistUiMessage())
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.AddToWishlist -> {
                viewModelScope.launch {
                    if (!session.isUserLoggedIn.first()) return@launch
                    wishlistMutationMutex.withLock {
                        setWishlistMutating(true)
                        try {
                            val currentlyFavorite = action.isWishListed
                            if (currentlyFavorite) {
                                when (wishlistRepository.removeFromWishlist(action.courseId)) {
                                    is Result.Success -> {
                                        _uiState.update { state ->
                                            if (state is CompareUiState.Content) {
                                                state.copy(
                                                    courses = state.courses.filter {
                                                        it.courseId != action.courseId
                                                    },
                                                    isWishlistMutating = false,
                                                )
                                            } else state
                                        }
                                    }
                                    is Result.Error -> Unit
                                }
                            } else {
                                when (wishlistRepository.addToWishlist(action.courseId)) {
                                    is Result.Success -> refreshWishlistsPreservingScreen()
                                    is Result.Error -> Unit
                                }
                            }
                        } finally {
                            setWishlistMutating(false)
                        }
                    }
                }
            }
        }
    }

    /** Refetch list without switching to full-screen loading (used after add). */
    private suspend fun refreshWishlistsPreservingScreen() {
        when (val r = wishlistRepository.getWishlists()) {
            is Result.Success -> _uiState.value =
                CompareUiState.Content(courses = r.data, isWishlistMutating = false)
            is Result.Error -> Unit
        }
    }

    private fun setWishlistMutating(active: Boolean) {
        _uiState.update { state ->
            if (state is CompareUiState.Content) state.copy(isWishlistMutating = active)
            else state
        }
    }
}

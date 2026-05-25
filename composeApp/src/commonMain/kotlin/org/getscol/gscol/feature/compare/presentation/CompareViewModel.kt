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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

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

    /**
     * Pull-to-refresh: refetch wishlist without switching to full-screen [CompareUiState.Loading].
     * Serialized with wishlist remove via [wishlistMutationMutex].
     */
    fun onRefresh() {
        viewModelScope.launch {
            wishlistMutationMutex.withLock {
                _isRefreshing.value = true
                try {
                    when (val r = wishlistRepository.getWishlists()) {
                        is Result.Success ->
                            _uiState.value = CompareUiState.Content(courses = r.data)

                        is Result.Error -> {
                            if (_uiState.value !is CompareUiState.Content) {
                                _uiState.value = CompareUiState.Error(r.error.asWishlistUiMessage())
                            }
                        }
                    }
                } finally {
                    _isRefreshing.value = false
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.AddToWishlist -> {
                viewModelScope.launch {
                    if (!session.isUserLoggedIn.first()) return@launch
                    // Wishlist screen only lists saved courses; user can only remove, not add.
                    if (!action.isWishListed) return@launch
                    wishlistMutationMutex.withLock {
                        setWishlistMutating(true)
                        try {
                            when (wishlistRepository.removeFromWishlist(action.courseId)) {
                                is Result.Success -> refetchWishlistsFromApi()
                                is Result.Error -> Unit
                            }
                        } finally {
                            setWishlistMutating(false)
                        }
                    }
                }
            }

            is HomeAction.OnHideLoginPromptBottomSheet -> {}
        }
    }

    /** GET /wishlists after a mutation; keeps list visible under the overlay loader (no full-screen Loading). */
    private suspend fun refetchWishlistsFromApi() {
        when (val r = wishlistRepository.getWishlists()) {
            is Result.Success ->
                _uiState.value = CompareUiState.Content(courses = r.data, isWishlistMutating = false)

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

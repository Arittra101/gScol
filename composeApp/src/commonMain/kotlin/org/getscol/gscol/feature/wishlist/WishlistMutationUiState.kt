package org.getscol.gscol.feature.wishlist

data class WishlistMutationUiState(
    val isMutating: Boolean = false,
    val showLoginPromptBottomSheet: Boolean = false
)

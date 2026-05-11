package org.getscol.gscol.feature.wishlist

/**
 * Wishlist add/remove in progress. Used with [BaseScreen] `showLoader`
 * on screens whose main list state stays in PagingData / separate flows.
 */
data class WishlistMutationUiState(val isMutating: Boolean = false)

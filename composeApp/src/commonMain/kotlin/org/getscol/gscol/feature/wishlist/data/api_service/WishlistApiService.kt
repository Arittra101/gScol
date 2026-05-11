package org.getscol.gscol.feature.wishlist.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.wishlist.data.dto.WishlistMutationDto
import org.getscol.gscol.feature.wishlist.data.dto.WishlistsResponseDto

interface WishlistApiService {
    suspend fun getWishlists(): Result<WishlistsResponseDto, DataError.Remote>
    suspend fun addToWishlist(courseId: String): Result<WishlistMutationDto, DataError.Remote>
    suspend fun removeFromWishlist(courseId: String): Result<WishlistMutationDto, DataError.Remote>
}

package org.getscol.gscol.feature.wishlist.domain.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.domain.model.Course

interface WishlistRepository {
    suspend fun getWishlists(): Result<List<Course>, DataError.Remote>
    suspend fun addToWishlist(courseId: String): Result<Unit, DataError.Remote>
    suspend fun removeFromWishlist(courseId: String): Result<Unit, DataError.Remote>
}

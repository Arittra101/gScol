package org.getscol.gscol.feature.wishlist.data.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.data.mapper.toCourse
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.wishlist.data.api_service.WishlistApiService
import org.getscol.gscol.feature.wishlist.domain.repository.WishlistRepository

class WishlistRepositoryImpl(
    private val api: WishlistApiService,
) : WishlistRepository {

    override suspend fun getWishlists(): Result<List<Course>, DataError.Remote> {
        return when (val r = api.getWishlists()) {
            is Result.Success -> Result.Success(
                r.data.payload?.wishlists.orEmpty().map { dto ->
                    dto.toCourse().copy(isWishListed = true)
                }
            )
            is Result.Error -> Result.Error(r.error)
        }
    }

    override suspend fun addToWishlist(courseId: String): Result<Unit, DataError.Remote> {
        return when (val r = api.addToWishlist(courseId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(r.error)
        }
    }

    override suspend fun removeFromWishlist(courseId: String): Result<Unit, DataError.Remote> {
        return when (val r = api.removeFromWishlist(courseId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(r.error)
        }
    }
}

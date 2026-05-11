package org.getscol.gscol.feature.wishlist.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.wishlist.data.dto.WishlistMutationDto
import org.getscol.gscol.feature.wishlist.data.dto.WishlistPutBody
import org.getscol.gscol.feature.wishlist.data.dto.WishlistsResponseDto

class WishlistApiServiceImpl(
    private val httpClient: HttpClient,
) : WishlistApiService {

    override suspend fun getWishlists(): Result<WishlistsResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.get("wishlists")
        }
    }

    override suspend fun addToWishlist(courseId: String): Result<WishlistMutationDto, DataError.Remote> {
        return safeApiCall {
            httpClient.put("wishlists") {
                contentType(ContentType.Application.Json)
                setBody(WishlistPutBody(courseId = courseId))
            }
        }
    }

    override suspend fun removeFromWishlist(courseId: String): Result<WishlistMutationDto, DataError.Remote> {
        return safeApiCall {
            httpClient.delete("wishlists/$courseId")
        }
    }
}

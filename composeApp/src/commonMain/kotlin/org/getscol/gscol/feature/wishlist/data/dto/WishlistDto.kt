package org.getscol.gscol.feature.wishlist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.feature.home.data.dto.CourseDto

@Serializable
data class WishlistsResponseDto(
    @SerialName("wishlists") val wishlists: List<CourseDto>? = null,
)

@Serializable
data class WishlistPutBody(
    @SerialName("courseId") val courseId: String,
)

@Serializable
data class WishlistMutationDto(
    @SerialName("success") val success: Boolean? = null,
    @SerialName("message") val message: String? = null,
)

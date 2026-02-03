package org.getscol.gscol.feature.home.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseRequest(
    @SerialName("pagination") val pagination: PaginationRequest,
    @SerialName("listType") val listType: String? = null
)

@Serializable
data class PaginationRequest(
    val cursor: String? = null,
    val limit: Int = 6
)
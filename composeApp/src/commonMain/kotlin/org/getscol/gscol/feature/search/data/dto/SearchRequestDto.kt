package org.getscol.gscol.feature.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.feature.home.domain.model.PaginationRequest

@Serializable
data class SearchRequestDto(
    @SerialName("pagination") val pagination: PaginationRequest,
    @SerialName("searchText") val searchText: String,
    @SerialName("listType") val listType: String = "ELIGIBLE_ONLY"
)

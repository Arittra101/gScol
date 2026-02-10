package org.getscol.gscol.feature.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.feature.home.domain.model.PaginationRequest

@Serializable
data class AdvancedSearchRequestDto(
    @SerialName("pagination") val pagination: PaginationRequest,
    @SerialName("searchText") val searchText: String,
    @SerialName("listType") val listType: String = "ELIGIBLE_ONLY",
    @SerialName("filters") val filters: AdvancedSearchFiltersDto? = null,
    @SerialName("ranges") val ranges: AdvancedSearchRangesDto? = null,
    @SerialName("flags") val flags: AdvancedSearchFlagsDto? = null
)

@Serializable
data class AdvancedSearchFiltersDto(
    @SerialName("countryIds") val countryIds: List<String>? = null,
    @SerialName("cityIds") val cityIds: List<String>? = null,
    @SerialName("programmeIds") val programmeIds: List<String>? = null,
    @SerialName("intakeIds") val intakeIds: List<String>? = null,
    @SerialName("intakeYear") val intakeYear: Int? = null
)

@Serializable
data class AdvancedSearchRangesDto(
    @SerialName("tuitionFee") val tuitionFee: MinMaxDto? = null,
    @SerialName("durationMonths") val durationMonths: MinMaxDto? = null
)

@Serializable
data class MinMaxDto(
    @SerialName("min") val min: Int? = null,
    @SerialName("max") val max: Int? = null
)

@Serializable
data class AdvancedSearchFlagsDto(
    @SerialName("hasScholarship") val hasScholarship: Boolean? = null
)

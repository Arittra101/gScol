package org.getscol.gscol.feature.search.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdvancedSearchParams(
    @SerialName("filters") val filters: AdvancedFilters? = null,
    @SerialName("ranges") val ranges: AdvancedRanges? = null,
    @SerialName("flags") val flags: AdvancedFlags? = null
)

@Serializable
data class AdvancedFilters(
    @SerialName("countryIds") val countryIds: List<String>? = null,
    @SerialName("cityIds") val cityIds: List<String>? = null,
    @SerialName("programmeIds") val programmeIds: List<String>? = null,
    @SerialName("intakeIds") val intakeIds: List<String>? = null,
    @SerialName("intakeYear") val intakeYear: Int? = null
)

@Serializable
data class AdvancedRanges(
    @SerialName("tuitionFee") val tuitionFee: MinMax? = null,
    @SerialName("durationMonths") val durationMonths: MinMax? = null
)

@Serializable
data class MinMax(
    @SerialName("min") val min: Int? = null,
    @SerialName("max") val max: Int? = null
)

@Serializable
data class AdvancedFlags(
    @SerialName("hasScholarship") val hasScholarship: Boolean? = null
)

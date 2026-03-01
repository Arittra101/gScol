package org.getscol.gscol.feature.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto

@Serializable
data class AdvancedFiltersResponseDto(
    @SerialName("data") val data: AdvancedFiltersDataDto? = null
) : BaseDto()

@Serializable
data class AdvancedFiltersDataDto(
    @SerialName("filters") val filters: List<FilterDto> = emptyList()
)

@Serializable
data class FilterDto(
    @SerialName("name") val name: String = "",
    @SerialName("values") val values: List<FilterValueDto> = emptyList()
)

@Serializable
data class FilterValueDto(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = ""
)

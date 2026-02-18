package org.getscol.gscol.feature.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto

@Serializable
data class CitiesResponseDto(
    @SerialName("data") val data: CitiesDataDto? = null
) : BaseDto()

@Serializable
data class CitiesDataDto(
    @SerialName("cities") val cities: List<CityDto> = emptyList()
)

@Serializable
data class CityDto(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = ""
)

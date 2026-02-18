package org.getscol.gscol.feature.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto
import org.getscol.gscol.feature.home.data.dto.CourseListDto

@Serializable
data class SearchResponseDto(
    @SerialName("data") val data: CourseListDto? = null
) : BaseDto()

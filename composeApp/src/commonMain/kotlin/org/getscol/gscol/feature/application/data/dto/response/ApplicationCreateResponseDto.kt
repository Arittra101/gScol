package org.getscol.gscol.feature.application.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto

@Serializable
data class ApplicationCreateResponseDto(
    @SerialName("data")
    val data: ApplicationCreateDataDto? = null
) : BaseDto()


@Serializable
data class ApplicationCreateDataDto(
    @SerialName("success") val success: Boolean? = null,
    @SerialName("applicationId") val applicationId: String? = null
)
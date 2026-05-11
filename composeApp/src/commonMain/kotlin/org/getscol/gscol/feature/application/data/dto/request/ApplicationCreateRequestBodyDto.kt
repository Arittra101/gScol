package org.getscol.gscol.feature.application.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationCreateRequestBodyDto(
    @SerialName("universityId") val universityId: String? = null,
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("intake") val intake: IntakeRequestDto? = null
)

@Serializable
data class IntakeRequestDto(
    @SerialName("intakeMonth") val intakeMonth: Int? = null,
    @SerialName("intakeYear") val intakeYear: Int? = null
)
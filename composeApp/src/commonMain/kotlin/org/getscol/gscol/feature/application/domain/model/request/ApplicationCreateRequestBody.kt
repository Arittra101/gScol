package org.getscol.gscol.feature.application.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationCreateRequestBody(
    @SerialName("universityId") val universityId: String? = null,
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("intake") val intake: IntakeRequest? = null
)

@Serializable
data class IntakeRequest(
    @SerialName("intakeMonth") val intakeMonth: Int? = null,
    @SerialName("intakeYear") val intakeYear: Int? = null
)
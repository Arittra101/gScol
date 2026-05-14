package org.getscol.gscol.feature.application.data.dto.response

import kotlinx.serialization.SerialName
import org.getscol.gscol.core.data.dto.BaseDto
import kotlinx.serialization.Serializable


@Serializable
data class ApplicationListResponseDto(
    @SerialName("data") val data: ApplicationListDataDto? = null,
) : BaseDto()

@Serializable
data class ApplicationListDataDto(
    @SerialName("applications") val applications: List<ApplicationDto>? = null,
)

@Serializable
data class ApplicationDto(
    @SerialName("applicationId") val applicationId: String? = null,
    @SerialName("applicationOverview") val applicationOverview: ApplicationOverviewDto? = null,
)

@Serializable
data class ApplicationOverviewDto(
    @SerialName("universityInfo") val universityInfo: UniversityInfoDto? = null,
    @SerialName("courseInfo") val courseInfo: CourseInfoDto? = null,
    @SerialName("intakeInfo") val intakeInfo: IntakeInfoDto? = null,
    @SerialName("currentStage") val currentStage: StageDto? = null,
    @SerialName("currentStatus") val currentStatus: StatusDto? = null,
    @SerialName("lastUpdatedAt") val lastUpdatedAt: String? = null,
)

@Serializable
data class UniversityInfoDto(
    @SerialName("universityId") val universityId: String? = null,
    @SerialName("universityName") val universityName: String? = null,
    @SerialName("universityLogoUrl") val universityLogoUrl: String? = null,
    @SerialName("universityCoverImageUrl") val universityCoverImageUrl: String? = null,
)

@Serializable
data class CourseInfoDto(
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("courseName") val courseName: String? = null,
)

@Serializable
data class IntakeInfoDto(
    @SerialName("intakeId") val intakeId: String? = null,
    @SerialName("intakeName") val intakeName: String? = null,
)

@Serializable
data class StageDto(
    @SerialName("stageCode") val stageCode: String? = null,
    @SerialName("stageName") val stageName: String? = null,
)

@Serializable
data class StatusDto(
    @SerialName("statusCode") val statusCode: String? = null,
    @SerialName("statusName") val statusName: String? = null,
)
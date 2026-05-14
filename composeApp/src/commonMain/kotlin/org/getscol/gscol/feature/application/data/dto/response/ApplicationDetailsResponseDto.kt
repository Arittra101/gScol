package org.getscol.gscol.feature.application.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto

@Serializable
data class ApplicationDetailsResponseDto(
    @SerialName("data") val data: ApplicationDataDto? = null,
) : BaseDto()

@Serializable
data class ApplicationDataDto(
    @SerialName("applicationId") val applicationId: String? = null,
    @SerialName("applicationSerialNumber") val applicationSerialNumber: String? = null,
    @SerialName("applicationOverview") val applicationOverview: ApplicationDetailsOverviewDto? = null,
    @SerialName("documentCheckLists") val documentCheckLists: List<DocumentCheckListDto>? = null,
)

@Serializable
data class ApplicationDetailsOverviewDto(
    @SerialName("universityInfo") val universityInfo: UniversityInfoApplicationDto? = null,
    @SerialName("courseInfo") val courseInfo: CourseInfoApplicationDto? = null,
    @SerialName("intakeInfo") val intakeInfo: IntakeInfoApplicationDto? = null,
    @SerialName("currentStage") val currentStage: CurrentStageDto? = null,
    @SerialName("currentStatus") val currentStatus: CurrentStatusDto? = null,
    @SerialName("appliedDate") val appliedDate: String? = null,
    @SerialName("lastUpdatedAt") val lastUpdatedAt: String? = null,
    @SerialName("assignedTo") val assignedTo: String? = null,
)

@Serializable
data class UniversityInfoApplicationDto(
    @SerialName("universityId") val universityId: String? = null,
    @SerialName("universityName") val universityName: String? = null,
    @SerialName("universityLogoUrl") val universityLogoUrl: String? = null,
    @SerialName("universityCoverImageUrl") val universityCoverImageUrl: String? = null,
)

@Serializable
data class CourseInfoApplicationDto(
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("courseName") val courseName: String? = null,
)

@Serializable
data class IntakeInfoApplicationDto(
    @SerialName("intakeMonth") val intakeMonth: String? = null,
    @SerialName("intakeYear") val intakeYear: String? = null,
)

@Serializable
data class CurrentStageDto(
    @SerialName("stageCode") val stageCode: String? = null,
    @SerialName("stageName") val stageName: String? = null,
    @SerialName("stageInformation") val stageInformation: String? = null,
)

@Serializable
data class CurrentStatusDto(
    @SerialName("statusCode") val statusCode: String? = null,
    @SerialName("statusName") val statusName: String? = null,
)

@Serializable
data class DocumentCheckListDto(
    @SerialName("documentType") val documentType: DocumentTypeDto? = null,
    @SerialName("isRequired") val isRequired: Boolean? = null,
    @SerialName("isMultipleAllowed") val isMultipleAllowed: Boolean? = null,
    @SerialName("overallStatus") val overallStatus: String? = null,
    @SerialName("allowedMimeTypes") val allowedMimeTypes: String? = null,
    @SerialName("maxFileSizeBytes") val maxFileSizeBytes: Long? = null,
    @SerialName("uploadedDocuments") val uploadedDocuments: List<UploadedDocumentDto>? = null,
)

@Serializable
data class DocumentTypeDto(
    @SerialName("documentTypeId") val documentTypeId: String? = null,
    @SerialName("documentTypeCode") val documentTypeCode: String? = null,
    @SerialName("documentTypeName") val documentTypeName: String? = null,
)

@Serializable
data class UploadedDocumentDto(
    @SerialName("applicationDocumentId") val applicationDocumentId: String? = null,
    @SerialName("fileName") val fileName: String? = null,
    @SerialName("overallStatus") val overallStatus: String? = null,
)
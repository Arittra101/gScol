package org.getscol.gscol.feature.application.domain.model.response

data class ApplicationDetailsResponse(
    val applicationId: String? = null,
    val applicationSerialNumber: String? = null,
    val applicationOverview: ApplicationOverviewModel? = null,
    val documentCheckLists: List<DocumentCheckListModel?>? = null,
)

data class ApplicationOverviewModel(
    val universityInfo: UniversityInfoApplicationModel? = null,
    val courseInfo: CourseInfoApplicationModel? = null,
    val intakeInfo: IntakeInfoApplicationModel? = null,
    val currentStage: CurrentStageModel? = null,
    val currentStatus: CurrentStatusModel? = null,
    val appliedDate: String? = null,
    val lastUpdatedAt: String? = null,
    val assignedTo: String? = null,
)

data class UniversityInfoApplicationModel(
    val universityId: String? = null,
    val universityName: String? = null,
    val universityLogoUrl: String? = null,
    val universityCoverImageUrl: String? = null,
)

data class CourseInfoApplicationModel(
    val courseId: String? = null,
    val courseName: String? = null,
)

data class IntakeInfoApplicationModel(
    val intakeMonth: String? = null,
    val intakeYear: String? = null,
)

data class CurrentStageModel(
    val stageCode: String? = null,
    val stageName: String? = null,
    val stageInformation: String? = null,
)

data class CurrentStatusModel(
    val statusCode: String? = null,
    val statusName: String? = null,
)

data class DocumentCheckListModel(
    val documentType: DocumentTypeModel? = null,
    val isRequired: Boolean? = null,
    val isMultipleAllowed: Boolean? = null,
    val overallStatus: String? = null,
    val allowedMimeTypes: List<String?>? = null,
    val maxFileSizeBytes: Long? = null,
    val uploadedDocuments: List<UploadedDocumentModel?>? = null,
)

data class DocumentTypeModel(
    val documentTypeId: String? = null,
    val documentTypeCode: String? = null,
    val documentTypeName: String? = null,
)

data class UploadedDocumentModel(
    val applicationDocumentId: String? = null,
    val fileName: String? = null,
    val overallStatus: String? = null,
)
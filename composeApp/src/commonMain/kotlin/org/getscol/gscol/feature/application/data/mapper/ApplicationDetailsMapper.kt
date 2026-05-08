package org.getscol.gscol.feature.application.data.mapper

import org.getscol.gscol.feature.application.data.dto.response.ApplicationDetailsResponseDto
import org.getscol.gscol.feature.application.data.dto.response.DocumentCheckListDto
import org.getscol.gscol.feature.application.data.dto.response.UploadedDocumentDto
import org.getscol.gscol.feature.application.domain.model.response.ApplicationDetails
import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckList
import org.getscol.gscol.feature.application.domain.model.response.UploadedDocument
import org.getscol.gscol.feature.application.presentation.ApplicationStageState
import org.getscol.gscol.feature.application.presentation.DocumentCategoryState

fun ApplicationDetailsResponseDto.toDomain(): ApplicationDetails =
    ApplicationDetails(
        universityCoverImageUrl = data?.applicationOverview?.universityInfo?.universityCoverImageUrl,
        universityName = data?.applicationOverview?.universityInfo?.universityName,
        intakeMonth = data?.applicationOverview?.intakeInfo?.intakeMonth,
        intakeYear = data?.applicationOverview?.intakeInfo?.intakeYear,
        courseName = data?.applicationOverview?.courseInfo?.courseName,
        applicationSerialNumber = data?.applicationSerialNumber,
        documentCheckLists = data?.documentCheckLists?.map { it.toDomain() }.orEmpty(),
    )

fun DocumentCheckListDto.toDomain(): DocumentCheckList =
    DocumentCheckList(
        documentTypeId = documentType?.documentTypeId,
        documentTypeName = documentType?.documentTypeName,
        isRequired = isRequired,
        isMultipleAllowed = isMultipleAllowed,
        overallStatus = overallStatus.toDocumentCategoryState(),
        allowedMimeTypes = allowedMimeTypes?.split(",")?.map { it.trim() }.orEmpty(),
        maxFileSizeBytes = maxFileSizeBytes,
        uploadedDocuments = uploadedDocuments?.map { it.toDomain() }.orEmpty(),
    )

fun UploadedDocumentDto.toDomain(): UploadedDocument =
    UploadedDocument(
        applicationDocumentId = applicationDocumentId,
        fileName = fileName,
        overallStatus = overallStatus,
    )


fun String?.toDocumentCategoryState(): DocumentCategoryState {
    return when (this?.uppercase()) {
        "PENDING" -> DocumentCategoryState.PENDING
        "IN_PROGRESS" -> DocumentCategoryState.IN_PROGRESS
        "VERIFIED" -> DocumentCategoryState.VERIFIED
        else -> DocumentCategoryState.PENDING
    }
}

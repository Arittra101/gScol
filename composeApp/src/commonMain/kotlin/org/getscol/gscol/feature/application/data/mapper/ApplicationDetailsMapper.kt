package org.getscol.gscol.feature.application.data.mapper

import org.getscol.gscol.feature.application.data.dto.response.ApplicationDetailsResponseDto
import org.getscol.gscol.feature.application.data.dto.response.DocumentCheckListDto
import org.getscol.gscol.feature.application.data.dto.response.UploadedDocumentDto
import org.getscol.gscol.feature.application.domain.model.response.ApplicationDetails
import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckList
import org.getscol.gscol.feature.application.domain.model.response.UploadedDocument

fun ApplicationDetailsResponseDto.toDomain(): ApplicationDetails =
    ApplicationDetails(
        universityCoverImageUrl = data?.applicationOverview?.universityInfo?.universityCoverImageUrl,
        universityName = data?.applicationOverview?.universityInfo?.universityName,
        intakeMonth = data?.applicationOverview?.intakeInfo?.intakeMonth,
        intakeYear = data?.applicationOverview?.intakeInfo?.intakeYear,
        courseName = data?.applicationOverview?.courseInfo?.courseName,
        applicationSerialNumber = data?.applicationSerialNumber,
        documentCheckLists = data?.documentCheckLists?.map { it.toDomain() },
    )

fun DocumentCheckListDto.toDomain(): DocumentCheckList =
    DocumentCheckList(
        documentTypeId = documentType?.documentTypeId,
        documentTypeName = documentType?.documentTypeName,
        isRequired = isRequired,
        isMultipleAllowed = isMultipleAllowed,
        overallStatus = overallStatus,
        allowedMimeTypes = allowedMimeTypes?.split(",")?.map { it.trim() },
        maxFileSizeBytes = maxFileSizeBytes,
        uploadedDocuments = uploadedDocuments?.map { it.toDomain() },
    )

fun UploadedDocumentDto.toDomain(): UploadedDocument =
    UploadedDocument(
        applicationDocumentId = applicationDocumentId,
        fileName = fileName,
        overallStatus = overallStatus,
    )

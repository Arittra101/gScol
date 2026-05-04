package org.getscol.gscol.feature.application.data.mapper

import org.getscol.gscol.feature.application.data.dto.response.ApplicationDetailsOverviewDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationDetailsResponseDto
import org.getscol.gscol.feature.application.data.dto.response.CourseInfoApplicationDto
import org.getscol.gscol.feature.application.data.dto.response.CurrentStageDto
import org.getscol.gscol.feature.application.data.dto.response.CurrentStatusDto
import org.getscol.gscol.feature.application.data.dto.response.DocumentCheckListDto
import org.getscol.gscol.feature.application.data.dto.response.DocumentTypeDto
import org.getscol.gscol.feature.application.data.dto.response.IntakeInfoApplicationDto
import org.getscol.gscol.feature.application.data.dto.response.UniversityInfoApplicationDto
import org.getscol.gscol.feature.application.data.dto.response.UploadedDocumentDto
import org.getscol.gscol.feature.application.domain.model.response.ApplicationDetailsResponse
import org.getscol.gscol.feature.application.domain.model.response.ApplicationOverviewModel
import org.getscol.gscol.feature.application.domain.model.response.CourseInfoApplicationModel
import org.getscol.gscol.feature.application.domain.model.response.CurrentStageModel
import org.getscol.gscol.feature.application.domain.model.response.CurrentStatusModel
import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckListModel
import org.getscol.gscol.feature.application.domain.model.response.DocumentTypeModel
import org.getscol.gscol.feature.application.domain.model.response.IntakeInfoApplicationModel
import org.getscol.gscol.feature.application.domain.model.response.UniversityInfoApplicationModel
import org.getscol.gscol.feature.application.domain.model.response.UploadedDocumentModel

fun ApplicationDetailsResponseDto.toDomain(): ApplicationDetailsResponse =
    ApplicationDetailsResponse(
        applicationId = data?.applicationId,
        applicationSerialNumber = data?.applicationSerialNumber,
        applicationOverview = data?.applicationOverview?.toDomain(),
        documentCheckLists = data?.documentCheckLists?.map { it.toDomain() },
    )

fun ApplicationDetailsOverviewDto.toDomain(): ApplicationOverviewModel =
    ApplicationOverviewModel(
        universityInfo = universityInfo?.toDomain(),
        courseInfo = courseInfo?.toDomain(),
        intakeInfo = intakeInfo?.toDomain(),
        currentStage = currentStage?.toDomain(),
        currentStatus = currentStatus?.toDomain(),
        appliedDate = appliedDate,
        lastUpdatedAt = lastUpdatedAt,
        assignedTo = assignedTo,
    )

fun UniversityInfoApplicationDto.toDomain(): UniversityInfoApplicationModel =
    UniversityInfoApplicationModel(
        universityId = universityId,
        universityName = universityName,
        universityLogoUrl = universityLogoUrl,
        universityCoverImageUrl = universityCoverImageUrl,
    )

fun CourseInfoApplicationDto.toDomain(): CourseInfoApplicationModel =
    CourseInfoApplicationModel(
        courseId = courseId,
        courseName = courseName,
    )

fun IntakeInfoApplicationDto.toDomain(): IntakeInfoApplicationModel =
    IntakeInfoApplicationModel(
        intakeMonth = intakeMonth,
        intakeYear = intakeYear,
    )

fun CurrentStageDto.toDomain(): CurrentStageModel =
    CurrentStageModel(
        stageCode = stageCode,
        stageName = stageName,
        stageInformation = stageInformation,
    )

fun CurrentStatusDto.toDomain(): CurrentStatusModel =
    CurrentStatusModel(
        statusCode = statusCode,
        statusName = statusName,
    )

fun DocumentCheckListDto.toDomain(): DocumentCheckListModel =
    DocumentCheckListModel(
        documentType = documentType?.toDomain(),
        isRequired = isRequired,
        isMultipleAllowed = isMultipleAllowed,
        overallStatus = overallStatus,
        allowedMimeTypes = allowedMimeTypes?.split(",")?.map { it.trim() },
        maxFileSizeBytes = maxFileSizeBytes,
        uploadedDocuments = uploadedDocuments?.map { it.toDomain() },
    )

fun DocumentTypeDto.toDomain(): DocumentTypeModel =
    DocumentTypeModel(
        documentTypeId = documentTypeId,
        documentTypeCode = documentTypeCode,
        documentTypeName = documentTypeName,
    )

fun UploadedDocumentDto.toDomain(): UploadedDocumentModel =
    UploadedDocumentModel(
        applicationDocumentId = applicationDocumentId,
        fileName = fileName,
        overallStatus = overallStatus,
    )
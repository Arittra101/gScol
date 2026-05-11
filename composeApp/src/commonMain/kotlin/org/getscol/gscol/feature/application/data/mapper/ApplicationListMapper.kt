package org.getscol.gscol.feature.application.data.mapper

import org.getscol.gscol.feature.application.data.dto.response.ApplicationDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationListResponseDto
import org.getscol.gscol.feature.application.data.dto.response.StageDto
import org.getscol.gscol.feature.application.domain.model.response.ApplicationInfo
import org.getscol.gscol.feature.application.domain.model.response.ApplicationListResponse
import org.getscol.gscol.feature.application.domain.model.response.Stage

fun ApplicationListResponseDto.toDomain(): ApplicationListResponse =
    ApplicationListResponse(
        applications = data?.applications
            ?.mapNotNull { it.toDomain() }
            ?: emptyList()
    )

fun ApplicationDto.toDomain(): ApplicationInfo? {
    val id = applicationId ?: return null

    return ApplicationInfo(
        applicationId = id,
        universityLogoUrl = applicationOverview?.universityInfo?.universityLogoUrl,
        universityName = applicationOverview?.universityInfo?.universityName,
        courseName = applicationOverview?.courseInfo?.courseName,
        currentStage = applicationOverview?.currentStage?.toDomain(),
    )
}

fun StageDto.toDomain(): Stage = Stage(stageCode = stageCode, applicationStageName = stageName)
package org.getscol.gscol.feature.application.data.mapper

import org.getscol.gscol.feature.application.data.dto.response.ApplicationProgressBarItemDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationStageDataDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationStageItemDto
import org.getscol.gscol.feature.application.domain.model.response.ApplicationStage
import org.getscol.gscol.feature.application.domain.model.response.ProgressItems
import org.getscol.gscol.feature.application.domain.model.response.StageItem
import org.getscol.gscol.feature.application.domain.model.response.StageState


fun ApplicationStageDataDto.toDomain(): ApplicationStage {
    return ApplicationStage(
        totalStages = totalStages ?: 0,
        completedStages = completedStages ?: 0,
        currentStage = currentStage?.toDomain(),
        progressItems = progressBarItems?.map { it.toDomain() } ?: emptyList()
    )
}

fun ApplicationStageItemDto.toDomain(): StageItem {
    return StageItem(
        stageCode = stageCode.orEmpty(),
        stageName = stageName.orEmpty()
    )
}

fun ApplicationProgressBarItemDto.toDomain(): ProgressItems {
    return ProgressItems(
        stageCode = stageCode.orEmpty(),
        stageName = stageName.orEmpty(),
        order = order ?: 0,
        state = state.toStageState()
    )
}

fun String?.toStageState(): StageState {
    return when (this?.uppercase()) {
        "COMPLETED" -> StageState.COMPLETED
        "CURRENT" -> StageState.CURRENT
        "UPCOMING" -> StageState.UPCOMING
        else -> StageState.UNKNOWN
    }
}
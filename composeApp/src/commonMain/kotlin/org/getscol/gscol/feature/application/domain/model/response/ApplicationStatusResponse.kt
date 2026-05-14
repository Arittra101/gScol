package org.getscol.gscol.feature.application.domain.model.response

import org.getscol.gscol.feature.application.presentation.ApplicationStageState

data class ApplicationStage(
    val totalStages: Int? = null,
    val completedStages: Int? = null,
    val currentStage: StageItem? = null,
    val progressItems: List<ProgressItems>? = null
) {
    fun getProgressPercentage(): String {
        return ((completedStages?.toDouble() ?: 0.0) / (totalStages?.toDouble()
            ?: 1.0) * (100f)).toInt().toString()
    }
}

data class StageItem(
    val stageCode: String? = null,
    val stageName: String? = null
)

data class ProgressItems(
    val stageCode: String? = null,
    val stageName: String? = null,
    val order: Int? = null,
    val state: ApplicationStageState? = null
)
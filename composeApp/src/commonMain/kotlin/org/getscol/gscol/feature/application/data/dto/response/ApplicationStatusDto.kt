package org.getscol.gscol.feature.application.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto


@Serializable
data class ApplicationStageResponseDto(
    @SerialName("data") val data: ApplicationStageDataDto? = null
) : BaseDto()

@Serializable
data class ApplicationStageDataDto(
    @SerialName("totalStages") val totalStages: Int? = null,
    @SerialName("completedStages") val completedStages: Int? = null,
    @SerialName("currentStage") val currentStage: ApplicationStageItemDto? = null,
    @SerialName("progressBarItems") val progressBarItems: List<ApplicationProgressBarItemDto>? = null
)

@Serializable
data class ApplicationStageItemDto(
    @SerialName("stageCode") val stageCode: String? = null,
    @SerialName("stageName") val stageName: String? = null
)

@Serializable
data class ApplicationProgressBarItemDto(
    @SerialName("stageCode") val stageCode: String? = null,
    @SerialName("stageName") val stageName: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("state") val state: String? = null
)
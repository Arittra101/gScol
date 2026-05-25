package org.getscol.gscol.feature.application.presentation

import androidx.compose.ui.graphics.Color
import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckList
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorCompleted
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorInProgress
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorPending

enum class DocumentCategoryState {
    PENDING,
    IN_PROGRESS,
    VERIFIED
}

enum class ApplicationStageState {
    COMPLETED,
    CURRENT,
    UPCOMING,
    UNKNOWN
}

fun ApplicationStageState.label(): String = when (this) {
    ApplicationStageState.COMPLETED -> "Completed"
    ApplicationStageState.CURRENT -> "Current"
    ApplicationStageState.UPCOMING -> "Upcoming"
    ApplicationStageState.UNKNOWN -> "Unknown"
}

fun ApplicationStageState.labelColor(): Color = when (this) {
    ApplicationStageState.COMPLETED -> ColorCompleted
    ApplicationStageState.CURRENT -> ColorInProgress
    ApplicationStageState.UPCOMING -> ColorPending
    ApplicationStageState.UNKNOWN -> ColorPending
}

fun ApplicationStageState.progressStateIcon(): String = when (this) {
    ApplicationStageState.COMPLETED -> "files/ic_complete.svg"
    ApplicationStageState.CURRENT -> "files/ic_in_progress.svg"
    ApplicationStageState.UPCOMING -> "files/ic_upcoming.svg"
    ApplicationStageState.UNKNOWN -> "files/ic_upcoming.svg"
}


data class DocumentProgressStep(
    val label: String,
    val status: DocumentCategoryState,
)

fun List<DocumentCheckList>.toDocumentProgressSteps(): List<DocumentProgressStep> =
    map { doc -> DocumentProgressStep(label = doc.documentTypeName.orEmpty(), status = doc.overallStatus) }
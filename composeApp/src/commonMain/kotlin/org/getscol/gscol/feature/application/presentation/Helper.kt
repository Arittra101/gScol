package org.getscol.gscol.feature.application.presentation

import androidx.compose.ui.graphics.Color
import org.getscol.gscol.feature.application.domain.model.response.StageState
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorCompleted
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorInProgress
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorPending


enum class ApplicationStageStatus {
    GENERIC,
    UNDER_REVIEW,
    OFFER,
    VISA,
    COMPLETED
}

data class StatusStyle1(
    val label: String,
    val backgroundColor: Color,
    val textColor: Color
)

fun ApplicationStageStatus.toStyle(): StatusStyle1 = when (this) {
    ApplicationStageStatus.GENERIC -> StatusStyle1(
        "Submitted",
        Color(0xFFFDE8EC),
        Color(0xFFB5294A)
    )

    ApplicationStageStatus.UNDER_REVIEW -> StatusStyle1(
        "Under Review",
        Color(0xFFFFF3CD),
        Color(0xFF8A6000)
    )

    ApplicationStageStatus.OFFER -> StatusStyle1("Offer", Color(0xFFE6F4EA), Color(0xFF2E7D32))
    ApplicationStageStatus.VISA -> StatusStyle1("Visa", Color(0xFFEEEDFE), Color(0xFF534AB7))
    ApplicationStageStatus.COMPLETED -> StatusStyle1(
        "Completed",
        Color(0xFFF1EFE8),
        Color(0xFF5F5E5A)
    )
}


fun StageState.label(): String = when (this) {
    StageState.COMPLETED -> "Completed"
    StageState.CURRENT -> "Current"
    StageState.UPCOMING -> "Upcoming"
    StageState.UNKNOWN -> "Unknown"
}

fun StageState.labelColor(): Color = when (this) {
    StageState.COMPLETED -> ColorCompleted
    StageState.CURRENT -> ColorInProgress
    StageState.UPCOMING -> ColorPending
    StageState.UNKNOWN -> ColorPending
}

fun StageState.progressStateIcon(): String = when (this) {
    StageState.COMPLETED -> "files/ic_complete.svg"
    StageState.CURRENT -> "files/ic_in_progress.svg"
    StageState.UPCOMING -> "files/ic_upcoming.svg"
    StageState.UNKNOWN -> "files/ic_upcoming.svg"
}
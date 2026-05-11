package org.getscol.gscol.feature.application.presentation

import androidx.compose.ui.graphics.Color
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
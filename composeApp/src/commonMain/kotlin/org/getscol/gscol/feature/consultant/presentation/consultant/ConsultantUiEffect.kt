package org.getscol.gscol.feature.consultant.presentation.consultant

sealed interface ConsultantUiEffect {
    data class NavigateToDetails(val consultantId: String) : ConsultantUiEffect
    data class OpenBookingUrl(val url: String) : ConsultantUiEffect
}


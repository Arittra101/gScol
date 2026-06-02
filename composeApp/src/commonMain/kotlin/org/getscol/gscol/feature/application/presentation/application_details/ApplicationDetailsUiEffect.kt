package org.getscol.gscol.feature.application.presentation.application_details

sealed interface ApplicationDetailsUiEffect {
    data class NavigateToApplicationTracker(val applicationId: String) : ApplicationDetailsUiEffect
}
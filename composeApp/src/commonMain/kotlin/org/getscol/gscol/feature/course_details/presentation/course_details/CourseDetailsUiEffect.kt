package org.getscol.gscol.feature.course_details.presentation.course_details

sealed interface CourseDetailsUiEffect {
    data object NavigateBack : CourseDetailsUiEffect
    data class ApplyNow(val courseId: String) : CourseDetailsUiEffect
    data object RedirectToLogin : CourseDetailsUiEffect
}

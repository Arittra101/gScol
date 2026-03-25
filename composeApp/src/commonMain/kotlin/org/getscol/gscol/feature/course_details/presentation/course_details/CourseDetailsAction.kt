package org.getscol.gscol.feature.course_details.presentation.course_details

sealed interface CourseDetailsAction {
    data class TabSelected(val index: Int) : CourseDetailsAction
    data object ApplyNow : CourseDetailsAction
    data object ReadMoreClicked : CourseDetailsAction
}

package org.getscol.gscol.feature.course_details.presentation.course_details

import org.getscol.gscol.feature.course_details.domain.model.CourseDetails

data class CourseDetailsState(
    val courseDetails: CourseDetails? = null,
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)

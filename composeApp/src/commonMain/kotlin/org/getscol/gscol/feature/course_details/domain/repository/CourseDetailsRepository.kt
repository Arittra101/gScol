package org.getscol.gscol.feature.course_details.domain.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails

interface CourseDetailsRepository {
    suspend fun getCourseDetails(courseId: String): Result<CourseDetails, DataError.Remote>
}


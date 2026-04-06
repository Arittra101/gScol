package org.getscol.gscol.feature.course_details.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.course_details.data.dto.CourseDetailsResponseDto

interface CourseDetailsApiService {
    suspend fun getCourseDetails(courseId: String): Result<CourseDetailsResponseDto, DataError.Remote>
}


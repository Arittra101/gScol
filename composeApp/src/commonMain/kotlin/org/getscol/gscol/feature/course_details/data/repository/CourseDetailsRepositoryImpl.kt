package org.getscol.gscol.feature.course_details.data.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.course_details.data.api_service.CourseDetailsApiService
import org.getscol.gscol.feature.course_details.data.mapper.toDomain
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import org.getscol.gscol.feature.course_details.domain.repository.CourseDetailsRepository

class CourseDetailsRepositoryImpl(
    private val api: CourseDetailsApiService,
) : CourseDetailsRepository {
    override suspend fun getCourseDetails(courseId: String): Result<CourseDetails, DataError.Remote> {
        return when (val apiResult = api.getCourseDetails(courseId)) {
            is Result.Error -> apiResult
            is Result.Success -> {
                val dto = apiResult.data
                val payload = dto.data
                val details = payload?.courseDetails ?: dto.courseDetails
                val meta = payload?.meta ?: dto.meta
                if (details == null) {
                    Result.Error(DataError.Remote.SERIALIZATION)
                } else {
                    Result.Success(details.toDomain(meta.orEmpty()))
                }
            }
        }
    }
}


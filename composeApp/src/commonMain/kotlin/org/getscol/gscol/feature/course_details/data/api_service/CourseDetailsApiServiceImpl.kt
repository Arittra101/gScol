package org.getscol.gscol.feature.course_details.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.course_details.data.dto.CourseDetailsResponseDto

class CourseDetailsApiServiceImpl(
    private val httpClient: HttpClient,
) : CourseDetailsApiService {

    override suspend fun getCourseDetails(courseId: String): Result<CourseDetailsResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.get("/courses/$courseId") {
                markAsNoAuth()
            }
        }
    }
}

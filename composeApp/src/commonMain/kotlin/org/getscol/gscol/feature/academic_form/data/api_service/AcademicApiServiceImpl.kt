package org.getscol.gscol.feature.academic_form.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoDtoResponse

class AcademicApiServiceImpl(
    private val httpClient: HttpClient
) : AcademicApiService {
    override suspend fun fetchAcademicInfo(): Result<AcademicInfoDtoResponse, DataError.Remote> {
        return safeApiCall {
            httpClient.get("/leads/profile/academic-form") {
                contentType(ContentType.Application.Json)
            }
        }
    }
}
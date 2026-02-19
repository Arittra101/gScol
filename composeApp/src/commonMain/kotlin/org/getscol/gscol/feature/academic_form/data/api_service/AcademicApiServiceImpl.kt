package org.getscol.gscol.feature.academic_form.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.dto.BaseDto
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoDtoResponse
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoRequest
import org.getscol.gscol.feature.auth.domain.model.ForgotPasswordRequest

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

    override suspend fun updateAcademicInfo(academicInfoRequest: AcademicInfoRequest): Result<BaseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.put("/leads/profile/academic-form") {
                contentType(ContentType.Application.Json)
                setBody(academicInfoRequest)
            }
        }
    }
}
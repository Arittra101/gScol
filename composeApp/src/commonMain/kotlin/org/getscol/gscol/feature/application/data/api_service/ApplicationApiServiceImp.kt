package org.getscol.gscol.feature.application.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.getscol.gscol.core.data.network.newSafeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.application.data.dto.request.ApplicationCreateRequestBodyDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationCreateResponseDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationDetailsResponseDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationListResponseDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationStageResponseDto

class ApplicationApiServiceImp(private val httpClient: HttpClient) : ApplicationApiService {
    override suspend fun getApplicationStatus(applicationId: String): Result<ApplicationStageResponseDto, DataError> {
        return newSafeApiCall {
            httpClient.get("/applications/${applicationId}/stage-progress")
        }
    }

    override suspend fun getApplicationList(): Result<ApplicationListResponseDto, DataError> {
        return newSafeApiCall {
            httpClient.get("applications")
        }
    }

    override suspend fun createApplication(applicationFormRequest: ApplicationCreateRequestBodyDto): Result<ApplicationCreateResponseDto, DataError> {
        return newSafeApiCall {
            httpClient.post("applications") {
                setBody(applicationFormRequest)
            }
        }
    }

    override suspend fun getApplicationById(applicationId: String): Result<ApplicationDetailsResponseDto, DataError> {
        return newSafeApiCall {
            httpClient.get("applications/${applicationId}")
        }
    }

    override suspend fun deleteDocument(
        applicationId: String,
        documentId: String
    ): Result<Unit, DataError> {
        return newSafeApiCall {
            httpClient.delete("applications/${applicationId}/documents/${documentId}")
        }
    }

}
package org.getscol.gscol.feature.application.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.application.data.dto.request.ApplicationCreateRequestBodyDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationCreateResponseDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationDetailsResponseDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationListResponseDto
import org.getscol.gscol.feature.application.data.dto.response.ApplicationStageResponseDto

interface ApplicationApiService {

    suspend fun getApplicationStatus(applicationId: String): Result<ApplicationStageResponseDto, DataError>
    suspend fun getApplicationList(): Result<ApplicationListResponseDto, DataError>
    suspend fun createApplication(applicationFormRequest: ApplicationCreateRequestBodyDto): Result<ApplicationCreateResponseDto, DataError>
    suspend fun getApplicationById(applicationId: String): Result<ApplicationDetailsResponseDto, DataError>
    suspend fun deleteDocument(applicationId:String, documentId: String): Result<Unit, DataError>

}
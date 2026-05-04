package org.getscol.gscol.feature.application.domain.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.domain.map
import org.getscol.gscol.feature.application.data.api_service.ApplicationApiService
import org.getscol.gscol.feature.application.data.mapper.toDomain
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.model.request.ApplicationCreateRequestBody
import org.getscol.gscol.feature.application.domain.model.response.ApplicationDetailsResponse
import org.getscol.gscol.feature.application.domain.model.response.ApplicationListResponse
import org.getscol.gscol.feature.application.domain.model.response.ApplicationStage
import toDto

class ApplicationRepositoryImp(private val applicationService: ApplicationApiService) :
    ApplicationRepository {

    override suspend fun getApplicationStatus(applicationId: String): Flow<Result<ApplicationStage, DataError>> {
        return flow {
            val result = applicationService.getApplicationStatus(applicationId)
            emit(result.map { it.data?.toDomain() ?: ApplicationStage() })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getApplicationList(): Flow<Result<ApplicationListResponse, DataError>> {
        return flow {
            val result = applicationService.getApplicationList()
            emit(result.map { it.toDomain() })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createApplication(applicationFormRequest: ApplicationCreateRequestBody): Flow<Result<String?, DataError>> {
        return flow {
            val result = applicationService.createApplication(applicationFormRequest.toDto())
            emit(result.map { it.data?.applicationId })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getApplicationById(applicationId: String): Flow<Result<ApplicationDetailsResponse, DataError>> {
        return flow {
            val result = applicationService.getApplicationById(applicationId)
            emit(result.map { it.toDomain() })
        }.flowOn(Dispatchers.IO)
    }

}
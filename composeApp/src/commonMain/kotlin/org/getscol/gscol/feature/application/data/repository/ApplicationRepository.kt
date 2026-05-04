package org.getscol.gscol.feature.application.data.repository

import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.application.domain.model.request.ApplicationCreateRequestBody
import org.getscol.gscol.feature.application.domain.model.response.ApplicationDetails
import org.getscol.gscol.feature.application.domain.model.response.ApplicationListResponse
import org.getscol.gscol.feature.application.domain.model.response.ApplicationStage

interface ApplicationRepository {
    suspend fun getApplicationStatus(applicationId: String): Flow<Result<ApplicationStage, DataError>>

    suspend fun getApplicationList() : Flow<Result<ApplicationListResponse, DataError>>

    suspend fun createApplication(applicationFormRequest: ApplicationCreateRequestBody) : Flow<Result<String?, DataError>>

    suspend fun getApplicationById(applicationId: String): Flow<Result<ApplicationDetails, DataError>>

}
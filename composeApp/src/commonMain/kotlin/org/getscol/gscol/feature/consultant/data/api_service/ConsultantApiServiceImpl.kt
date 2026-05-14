package org.getscol.gscol.feature.consultant.data.api_service

import org.getscol.gscol.feature.consultant.data.local_data_source.LocalConsultantDataSource
import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel


/**
 * LocalConsultantApiService
 *
 * Implements [ConsultantApiService] using the local JSON data source.
 * Swap this with real implementation when
 * API is ready — the Repository won't need any changes.
 */
class ConsultantApiServiceImpl : ConsultantApiService {
    override suspend fun getConsultants(): List<ConsultantModel> {
        return LocalConsultantDataSource.getConsultants()
    }

    override suspend fun getConsultantById(id: String): ConsultantModel? {
        return LocalConsultantDataSource.getConsultantById(id)
    }

}
package org.getscol.gscol.feature.consultant.data.api_service

import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel

interface ConsultantApiService {
    suspend fun getConsultants(): List<ConsultantModel>
    suspend fun getConsultantById(id: String): ConsultantModel?
}
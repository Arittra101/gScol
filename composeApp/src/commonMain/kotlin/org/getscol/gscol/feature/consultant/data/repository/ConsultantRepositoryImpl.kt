package org.getscol.gscol.feature.consultant.data.repository

import org.getscol.gscol.feature.consultant.data.api_service.ConsultantApiService
import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel
import org.getscol.gscol.feature.consultant.domain.repository.ConsultantRepository

class ConsultantRepositoryImpl(
    private val apiService: ConsultantApiService
) : ConsultantRepository {
    override suspend fun getConsultants(): Result<List<ConsultantModel>> {
        return try {
            Result.success(apiService.getConsultants())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConsultantById(id: String): Result<ConsultantModel?> {
        return try {
            Result.success(apiService.getConsultantById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
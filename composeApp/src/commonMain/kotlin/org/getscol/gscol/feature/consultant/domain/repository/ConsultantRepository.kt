package org.getscol.gscol.feature.consultant.domain.repository

import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel

interface ConsultantRepository {
    suspend fun getConsultants(): Result<List<ConsultantModel>>
    suspend fun getConsultantById(id: String): Result<ConsultantModel?>
}
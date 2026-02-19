package org.getscol.gscol.feature.academic_form.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.domain.asUnit
import org.getscol.gscol.core.domain.map
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoRequest
import org.getscol.gscol.feature.academic_form.data.api_service.AcademicApiService
import org.getscol.gscol.feature.academic_form.data.mapper.toAcademicProfile
import org.getscol.gscol.feature.academic_form.domain.model.AcademicProfile
import org.getscol.gscol.feature.academic_form.domain.repository.AcademicRepository

class AcademicRepoImpl(private val academicApiService: AcademicApiService) : AcademicRepository {
    override fun fetchAcademicInfo(): Flow<Result<AcademicProfile, DataError.Remote>> = flow {
        val result = academicApiService.fetchAcademicInfo()
        emit(result.map { it.toAcademicProfile() })
    }

    override suspend fun updateAcademicInfo(academicInfoRequest: AcademicInfoRequest): Result<Unit, DataError.Remote> {
        return academicApiService.updateAcademicInfo(academicInfoRequest).asUnit()
    }
}

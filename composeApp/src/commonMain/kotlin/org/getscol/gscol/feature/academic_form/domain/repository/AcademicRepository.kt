package org.getscol.gscol.feature.academic_form.domain.repository

import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoRequest
import org.getscol.gscol.feature.academic_form.domain.model.AcademicProfile

interface AcademicRepository {
    fun fetchAcademicInfo(): Flow<Result<AcademicProfile, DataError.Remote>>

    suspend fun updateAcademicInfo(academicInfoRequest: AcademicInfoRequest): Result<Unit, DataError.Remote>
}
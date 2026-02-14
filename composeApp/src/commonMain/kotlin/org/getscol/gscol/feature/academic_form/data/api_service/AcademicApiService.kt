package org.getscol.gscol.feature.academic_form.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoDtoResponse

interface AcademicApiService {
    suspend fun fetchAcademicInfo(): Result<AcademicInfoDtoResponse, DataError.Remote>
}
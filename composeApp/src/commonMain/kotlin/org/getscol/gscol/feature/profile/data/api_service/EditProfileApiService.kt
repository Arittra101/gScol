package org.getscol.gscol.feature.profile.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.data.dto.EditProfileDtoResponse

interface EditProfileApiService {
    suspend fun fetchEditProfile(): Result<EditProfileDtoResponse, DataError.Remote>
}


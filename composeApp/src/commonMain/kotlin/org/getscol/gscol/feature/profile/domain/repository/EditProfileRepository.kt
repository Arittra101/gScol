package org.getscol.gscol.feature.profile.domain.repository

import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.domain.model.EditProfile

interface EditProfileRepository {
    fun fetchEditProfile(): Flow<Result<EditProfile, DataError.Remote>>
}


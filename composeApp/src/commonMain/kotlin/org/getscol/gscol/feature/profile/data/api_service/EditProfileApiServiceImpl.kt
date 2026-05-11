package org.getscol.gscol.feature.profile.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.data.dto.EditProfileDtoResponse

class EditProfileApiServiceImpl(
    private val client: HttpClient,
) : EditProfileApiService {
    override suspend fun fetchEditProfile(): Result<EditProfileDtoResponse, DataError.Remote> =
        safeApiCall { client.get("leads/profile") }
}

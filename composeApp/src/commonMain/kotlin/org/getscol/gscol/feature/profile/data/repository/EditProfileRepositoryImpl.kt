package org.getscol.gscol.feature.profile.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.domain.map
import org.getscol.gscol.feature.profile.data.api_service.DocumentDownloadApiService
import org.getscol.gscol.feature.profile.data.api_service.EditProfileApiService
import org.getscol.gscol.feature.profile.data.mapper.toDomain
import org.getscol.gscol.feature.profile.domain.model.EditProfile
import org.getscol.gscol.feature.profile.domain.repository.EditProfileRepository

class EditProfileRepositoryImpl(
    private val apiService: EditProfileApiService,
    private val documentDownloadApiService: DocumentDownloadApiService,
) : EditProfileRepository {
    override fun fetchEditProfile(): Flow<Result<EditProfile, DataError.Remote>> = flow {
        val result = apiService.fetchEditProfile()
        emit(result.map { it.toDomain() })
    }

    override suspend fun getDocumentDownloadUrl(documentId: String): Result<String, DataError.Remote> {
        return when (val result = documentDownloadApiService.requestDownloadLink(documentId)) {
            is Result.Success -> {
                val url = result.data.data?.url
                if (url.isNullOrBlank()) {
                    Result.Error(DataError.Remote.SERIALIZATION)
                } else {
                    Result.Success(url)
                }
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}

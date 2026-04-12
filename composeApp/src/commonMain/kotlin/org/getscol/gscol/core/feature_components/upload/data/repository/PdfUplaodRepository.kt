package org.getscol.gscol.core.feature_components.upload.data.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlResponse
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody

interface PdfUploadRepository {
    suspend fun getPdfUploadUrl(urlRequestBody: UploadUrlRequestBody): Result<UploadUrlResponse, DataError>

    suspend fun uploadToS3(
        uploadUrl: String,
        fileBytes: ByteArray,
        uploadProgress: (Long) -> Unit
    ): Result<Unit, DataError>

    suspend fun confirmUpload(versionId: String): Result<Unit, DataError>
}
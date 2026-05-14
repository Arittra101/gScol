package org.getscol.gscol.core.feature_components.upload.data.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlResponse

interface PdfUploadRepository {
    suspend fun getPdfUploadUrl(
        applicationId: String,
        documentTypeId: String,
        urlRequestBody: UploadUrlRequestBody
    ): Result<UploadUrlResponse, DataError>

    suspend fun uploadToS3(
        uploadUrl: String,
        fileBytes: ByteArray,
        uploadProgress: (Float) -> Unit
    ): Result<Unit, DataError>

    suspend fun confirmUpload(
        applicationId: String,
        documentTypeId: String,
        documentId: String,
        documentVersionId: String
    ): Result<Unit, DataError>
}
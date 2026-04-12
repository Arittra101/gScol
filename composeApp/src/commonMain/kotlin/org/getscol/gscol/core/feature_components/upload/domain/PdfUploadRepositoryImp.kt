package org.getscol.gscol.core.feature_components.upload.domain

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlResponse
import org.getscol.gscol.core.feature_components.upload.data.repository.PdfUploadRepository
import org.getscol.gscol.core.feature_components.upload.data.service.PdfUploaderService

class PdfUploadRepositoryImp(private val service: PdfUploaderService) : PdfUploadRepository {

    override suspend fun getPdfUploadUrl(urlRequestBody: UploadUrlRequestBody): Result<UploadUrlResponse, DataError> {
        return service.getPdfUploadUrl(urlRequestBody)
    }

    override suspend fun uploadToS3(
        uploadUrl: String,
        fileBytes: ByteArray,
        uploadProgress: (Long) -> Unit
    ): Result<Unit, DataError> {
        return service.uploadToS3(uploadUrl, fileBytes,uploadProgress)
    }

    override suspend fun confirmUpload(versionId: String): Result<Unit, DataError> {
        return service.confirmUpload(versionId)
    }

}
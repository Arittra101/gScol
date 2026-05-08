package org.getscol.gscol.core.feature_components.upload

import org.getscol.gscol.core.domain.DocumentError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.domain.checkSuccessResponse
import org.getscol.gscol.core.domain.toData
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody
import org.getscol.gscol.core.feature_components.upload.data.repository.PdfUploadRepository
import org.getscol.gscol.core.feature_components.upload.domain.model.UploadPdfModel

class PdfUploader(private val repository: PdfUploadRepository) {

    data class UploadedPdf(
        val documentTypeId: String,
        val documentId: String,
        val documentName: String
    )

    suspend fun uploadPdf(
        uploadPdfModel: UploadPdfModel,
        uploadProgress: (Float) -> Unit,
    ): Result<UploadedPdf, DocumentError> {
        //fetching upload url from the backend
        val pickedFile = uploadPdfModel.pickedFile

        val uploadUrlRequestBody = UploadUrlRequestBody(
            fileName = pickedFile.fileName,
            mimeType = pickedFile.mimeType,
            fileSizeBytes = pickedFile.fileByteSize
        )
        println("Our pick ${uploadUrlRequestBody}")


        // collect first emission from flow-based repository methods
        val pdfUploadUrlResponse = repository.getPdfUploadUrl(
            uploadPdfModel.applicationId,
            uploadPdfModel.documentTypeId,
            uploadUrlRequestBody,
        )

        val uploadUrlData = pdfUploadUrlResponse.toData()
        uploadProgress(0.1f)

        if (!checkSuccessResponse(pdfUploadUrlResponse) || uploadUrlData?.data?.uploadUrl == null || uploadUrlData.data.documentVersionId == null || uploadUrlData.data.documentId == null) {
            return Result.Error(DocumentError.PdfUploadError)
        }

        //upload file to cloud by upload url
        val uploadToS3Response =
            repository.uploadToS3(uploadUrlData.data.uploadUrl, pickedFile.bytes) {
                uploadProgress(0.1f + it * 0.8f)
            }

        if (!checkSuccessResponse(uploadToS3Response)) {
            return Result.Error(DocumentError.PdfUploadError)
        }


        println("my document id ${uploadUrlData.data.documentId}")
        //confirm upload to backend by versionId
        val confirmUploadResponse = repository.confirmUpload(
            uploadPdfModel.applicationId,
            uploadPdfModel.documentTypeId,
            uploadUrlData.data.documentId,
            uploadUrlData.data.documentVersionId
        )
        val isSuccessfullyUploaded = checkSuccessResponse(confirmUploadResponse)

        return if (isSuccessfullyUploaded) {
            uploadProgress(1f)
            Result.Success(
                UploadedPdf(
                    uploadPdfModel.documentTypeId,
                    uploadUrlData.data.documentId,
                    documentName = pickedFile.fileName
                )
            )
        } else {
            Result.Error(DocumentError.PdfUploadError)
        }
    }
}
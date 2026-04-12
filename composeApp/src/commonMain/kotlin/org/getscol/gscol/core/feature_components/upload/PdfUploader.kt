package org.getscol.gscol.core.feature_components.upload

import org.getscol.gscol.core.domain.checkSuccessResponse
import org.getscol.gscol.core.domain.toData
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody
import org.getscol.gscol.core.feature_components.upload.data.repository.PdfUploadRepository
import org.getscol.gscol.core.feature_components.upload.domain.model.UploadPdfModel

class PdfUploader(private val repository: PdfUploadRepository) {

    suspend fun uploadPdf(
        uploadPdfModel: UploadPdfModel,
        uploadProgress: (Long) -> Unit,
    ): Boolean {
        //fetching upload url from the backend
        val pickedFile = uploadPdfModel.pickedFile

        val getUrlRequestBody = UploadUrlRequestBody(
            documentId = uploadPdfModel.documentTypeId,
            mimeType = pickedFile.mimeType,
            fileName = pickedFile.fileName,
            fileSizeBytes = pickedFile.fileByteSize
        )

        val pdfUploadUrlResponse = repository.getPdfUploadUrl(getUrlRequestBody)

        val uploadUrlData = pdfUploadUrlResponse.toData()
        if (!checkSuccessResponse(pdfUploadUrlResponse) || uploadUrlData?.data?.uploadUrl == null || uploadUrlData.data.versionId == null) {
            return false
        }


        //upload file to cloud by upload url
        val uploadToS3Response = repository.uploadToS3(uploadUrlData.data.uploadUrl, pickedFile.bytes, uploadProgress)
        if (!checkSuccessResponse(uploadToS3Response)) {
            return false
        }


        //confirm upload to backend by versionId
        val confirmUploadResponse = repository.confirmUpload(uploadUrlData.data.versionId)
        if (!checkSuccessResponse(confirmUploadResponse)) {
            return false
        }


        return true
    }
}
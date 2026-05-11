package org.getscol.gscol.core.feature_components.upload.data.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.newSafeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlResponse

class PdfUploaderServiceImp(private val httpClient: HttpClient) : PdfUploaderService {
    override suspend fun getPdfUploadUrl(
        applicationId: String,
        documentTypeId: String,
        urlRequestBody: UploadUrlRequestBody
    ): Result<UploadUrlResponse, DataError> {
        return newSafeApiCall {
            httpClient.post("/applications/$applicationId/document-types/$documentTypeId/upload-url") {
                contentType(ContentType.Application.Json)
                setBody(urlRequestBody)
            }
        }
    }

    override suspend fun uploadToS3(
        uploadUrl: String,
        fileBytes: ByteArray,
        uploadProgress: (Float) -> Unit
    ): Result<Unit, DataError> {
        return newSafeApiCall {
            httpClient.put(uploadUrl) {
                contentType(ContentType.Application.Pdf)
                headers { append(HttpHeaders.ContentLength, fileBytes.size.toString()) }
                setBody(fileBytes)
                markAsNoAuth()
                onUpload { byteSent, totalByte ->
                    val progress = totalByte
                        ?.takeIf { it > 0L }
                        ?.let { byteSent.toFloat() / it.toFloat() } ?: 0f

                    uploadProgress(progress)
                }
            }
        }
    }

    override suspend fun confirmUpload(
        applicationId: String,
        documentTypeId: String,
        documentId: String,
        documentVersionId: String
    ): Result<Unit, DataError> {
        return newSafeApiCall {
            httpClient.post("/applications/$applicationId/document-types/$documentTypeId/confirm-upload") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "documentId" to documentId,
                    "documentVersionId" to documentVersionId
                ))
            }
        }
    }

}
package org.getscol.gscol.core.feature_components.upload.data.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.newSafeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlRequestBody
import org.getscol.gscol.core.feature_components.upload.data.model.UploadUrlResponse

class PdfUploaderServiceImp(private val httpClient: HttpClient) : PdfUploaderService {
    override suspend fun getPdfUploadUrl(urlRequestBody: UploadUrlRequestBody): Result<UploadUrlResponse, DataError> {
        return newSafeApiCall {
            httpClient.post("https://scol-backend-document-demo.vercel.app/documents/upload-url") {
                contentType(ContentType.Application.Json)
                setBody(urlRequestBody)
            }
        }
    }

    override suspend fun uploadToS3(
        uploadUrl: String,
        fileBytes: ByteArray,
        uploadProgress: (Long) -> Unit
    ): Result<Unit, DataError> {
        return newSafeApiCall {
            httpClient.put(uploadUrl) {
                contentType(ContentType.Application.Pdf)
                headers { append(HttpHeaders.ContentLength, fileBytes.size.toString()) }
                setBody(fileBytes)
                onUpload { byteSent, totalByte ->
                    uploadProgress(29)
                }
            }
        }
    }

    override suspend fun confirmUpload(versionId: String): Result<Unit, DataError> {
        return newSafeApiCall {
            httpClient.post("documents/confirm-upload") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("versionId" to versionId))
            }
        }
    }

}
package org.getscol.gscol.auth.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.auth.domain.model.LoginRequest
import org.getscol.gscol.core.data.auth.AuthTokenResponse
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class AuthApiServiceImpl(
    private val httpClient: HttpClient
) : AuthApiService {

    override suspend fun login(
        phoneNumber: String,
        password: String
    ): Result<AuthTokenResponse, DataError.Remote> {
        return safeApiCall {
            httpClient.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(phoneNumber = phoneNumber, password = password))
                markAsNoAuth()
            }
        }
    }
}
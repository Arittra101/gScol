package org.getscol.gscol.auth.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.getscol.gscol.auth.data.dto.OtpReqResponseDto
import org.getscol.gscol.auth.data.dto.VerifyResponseDto
import org.getscol.gscol.auth.domain.OtpRequestData
import org.getscol.gscol.auth.domain.VerifyOtpRequestData
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class OtpAuthServiceImp(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : OtpAuthService {
    override suspend fun requestOtp(otpRequestData: OtpRequestData): Result<OtpReqResponseDto, DataError.Remote> {
        return safeApiCall<OtpReqResponseDto> {
            httpClient.post("$baseUrl/otp-login") {
                setBody(otpRequestData)
            }
        }
    }

    override suspend fun verifyOtp(verifyOtpRequestData: VerifyOtpRequestData): Result<VerifyResponseDto, DataError.Remote> {
        return safeApiCall<VerifyResponseDto> {
            httpClient.post("$baseUrl/otp-login") {
                setBody(verifyOtpRequestData)
            }
        }
    }

}
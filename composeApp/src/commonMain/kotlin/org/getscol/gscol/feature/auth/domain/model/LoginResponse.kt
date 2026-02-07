package org.getscol.gscol.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("status") val status: String,
    @SerialName("message") val message: String,
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data") val data: LoginData
)

@Serializable
data class LoginData(
    @SerialName("userId") val userId: String? = null,
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("expire_time") val expireTime: String? = null
)


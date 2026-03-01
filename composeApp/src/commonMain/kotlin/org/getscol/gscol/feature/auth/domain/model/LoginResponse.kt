package org.getscol.gscol.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("statusCode") val statusCode: Int? = null,
    @SerialName("data") val data: LoginData? = null
)

@Serializable
data class LoginData(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("expire_time") val expireTime: String? = null,
    @SerialName("user") val user: User? = null

)

@Serializable
data class User(
    @SerialName("userId") val userId: String? = null,
    @SerialName("academicFormStatus") val academicFormStatus: String? = null,
)


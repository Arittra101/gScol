package org.getscol.gscol.auth.data.authdto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthPassResetReqDto(
    @SerialName("newPassword") val newPassword: String? = null,
)
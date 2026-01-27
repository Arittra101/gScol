package org.getscol.gscol.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    @SerialName("phone") val phone: String?=null,
    @SerialName("newPassword") val newPassword: String?=null,
)

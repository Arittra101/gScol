package org.getscol.gscol.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    @SerialName("phone") val phone: String
)

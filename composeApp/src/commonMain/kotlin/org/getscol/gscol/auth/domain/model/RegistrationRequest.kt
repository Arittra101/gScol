package org.getscol.gscol.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    @SerialName("phone") val phone: String,
    @SerialName("password") val password: String,
    @SerialName("fullName") val fullName: String
)

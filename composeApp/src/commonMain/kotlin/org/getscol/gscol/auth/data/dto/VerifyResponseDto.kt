package org.getscol.gscol.auth.data.dto

import kotlinx.serialization.SerialName
import org.getscol.gscol.core.data.dto.BaseResponseDto

data class VerifyResponseDto(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null
) : BaseResponseDto()

package org.getscol.gscol.home.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

import org.getscol.gscol.home.data.dto.HomeResponseDto

interface HomeApiService {
    suspend fun getHomeData(page: Int, limit: Int, isLogin: Boolean): Result<HomeResponseDto, DataError.Remote>
}

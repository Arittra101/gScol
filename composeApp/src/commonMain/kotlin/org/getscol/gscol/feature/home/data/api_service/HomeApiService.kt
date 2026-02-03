package org.getscol.gscol.feature.home.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

import org.getscol.gscol.feature.home.data.dto.HomeResponseDto
import org.getscol.gscol.feature.home.domain.model.CourseRequest

interface HomeApiService {
    suspend fun getHomeData(courseRequest: CourseRequest, isLogin: Boolean): Result<HomeResponseDto, DataError.Remote>
}

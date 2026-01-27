package org.getscol.gscol.home.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.home.data.dto.HomeResponseDto

class HomeApiServiceImpl(private val httpClient: HttpClient) : HomeApiService {
    override suspend fun getHomeData(page: Int, limit: Int, isLogin: Boolean): Result<HomeResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.get("api/home/courses") {
                contentType(ContentType.Application.Json)
                parameter("page", page)
                parameter("limit", limit)
                if (!isLogin) markAsNoAuth()
            }
        }
    }
}
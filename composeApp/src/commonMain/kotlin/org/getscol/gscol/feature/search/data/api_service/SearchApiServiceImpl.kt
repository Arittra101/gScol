package org.getscol.gscol.feature.search.data.api_service

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchRequestDto
import org.getscol.gscol.feature.search.data.dto.SearchRequestDto
import org.getscol.gscol.feature.search.data.dto.SearchResponseDto
import io.ktor.client.HttpClient

class SearchApiServiceImpl(private val httpClient: HttpClient) : SearchApiService {
    override suspend fun search(request: SearchRequestDto): Result<SearchResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.post("/search") {
                contentType(ContentType.Application.Json)
                setBody(request)
                markAsNoAuth()
            }
        }
    }

    override suspend fun advancedSearch(request: AdvancedSearchRequestDto): Result<SearchResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.post("/search/advanced") {
                contentType(ContentType.Application.Json)
                setBody(request)
                markAsNoAuth()
            }
        }
    }
}

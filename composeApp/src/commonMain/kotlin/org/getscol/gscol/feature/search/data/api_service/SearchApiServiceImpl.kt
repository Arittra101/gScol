package org.getscol.gscol.feature.search.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.search.data.dto.AdvancedFiltersResponseDto
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchRequestDto
import org.getscol.gscol.feature.search.data.dto.CitiesResponseDto
import org.getscol.gscol.feature.search.data.dto.SearchRequestDto
import org.getscol.gscol.feature.search.data.dto.SearchResponseDto

class SearchApiServiceImpl(
    private val httpClient: HttpClient,
    private val session: Session
) : SearchApiService {
    override suspend fun search(request: SearchRequestDto): Result<SearchResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.post("/search") {
                contentType(ContentType.Application.Json)
                setBody(request)
                if (!session.isUserLoggedIn.value) markAsNoAuth()
            }
        }
    }

    override suspend fun advancedSearch(request: AdvancedSearchRequestDto): Result<SearchResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.post("/search/advanced") {
                contentType(ContentType.Application.Json)
                setBody(request)
                if (!session.isUserLoggedIn.value) markAsNoAuth()
            }
        }
    }

    override suspend fun getAdvancedFilters(): Result<AdvancedFiltersResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.get("/search/advanced/filters") {
                markAsNoAuth()
            }
        }
    }

    override suspend fun getCities(countryId: String): Result<CitiesResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.get("/categories/cities") {
                parameter("countryId", countryId)
                markAsNoAuth()
            }
        }
    }
}

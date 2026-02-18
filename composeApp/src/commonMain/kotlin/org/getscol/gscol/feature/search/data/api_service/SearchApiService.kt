package org.getscol.gscol.feature.search.data.api_service

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.search.data.dto.AdvancedFiltersResponseDto
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchRequestDto
import org.getscol.gscol.feature.search.data.dto.CitiesResponseDto
import org.getscol.gscol.feature.search.data.dto.SearchRequestDto
import org.getscol.gscol.feature.search.data.dto.SearchResponseDto

interface SearchApiService {
    suspend fun search(request: SearchRequestDto): Result<SearchResponseDto, DataError.Remote>
    suspend fun advancedSearch(request: AdvancedSearchRequestDto): Result<SearchResponseDto, DataError.Remote>
    suspend fun getAdvancedFilters(): Result<AdvancedFiltersResponseDto, DataError.Remote>
    suspend fun getCities(countryId: String): Result<CitiesResponseDto, DataError.Remote>
}

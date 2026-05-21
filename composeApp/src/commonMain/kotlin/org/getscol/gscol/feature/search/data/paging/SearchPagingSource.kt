package org.getscol.gscol.feature.search.data.paging

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.data.mapper.toCourses
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.domain.model.PaginationRequest
import org.getscol.gscol.feature.search.data.api_service.SearchApiService
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchFiltersDto
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchFlagsDto
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchRequestDto
import org.getscol.gscol.feature.search.data.dto.IntakeFilterDto
import org.getscol.gscol.feature.search.data.dto.AdvancedSearchRangesDto
import org.getscol.gscol.feature.search.data.dto.MinMaxDto
import org.getscol.gscol.feature.search.data.dto.SearchRequestDto
import org.getscol.gscol.feature.search.domain.model.AdvancedFilters
import org.getscol.gscol.feature.search.domain.model.AdvancedFlags
import org.getscol.gscol.feature.search.domain.model.AdvancedRanges
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.domain.model.MinMax

class SearchPagingSource(
    private val searchApiService: SearchApiService,
    private val searchText: String,
    private val advancedParams: AdvancedSearchParams? = null
) : PagingSource<String, Course>() {

    override fun getRefreshKey(state: PagingState<String, Course>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Course> {
        return try {
            val cursor = params.key
            val pagination = PaginationRequest(cursor = cursor, limit = 15)
            val result = if (advancedParams == null) {
                searchApiService.search(
                    SearchRequestDto(
                        pagination = pagination,
                        searchText = searchText,
                    )
                )
            } else {
                searchApiService.advancedSearch(
                    AdvancedSearchRequestDto(
                        pagination = pagination,
                        filters = advancedParams.filters?.toDto(),
                        ranges = advancedParams.ranges?.toDto(),
                        flags = advancedParams.flags?.toDto()
                    )
                )
            }
            when (result) {
                is Result.Success -> {
                    val courses = result.data.data?.courses.orEmpty()
                    val nextCursor = result.data.data?.pagination?.cursor
                    val hasNext = result.data.data?.pagination?.hasNext == true
                    LoadResult.Page(
                        data = courses.toCourses(),
                        prevKey = null,
                        nextKey = if (hasNext && nextCursor != null) nextCursor else null
                    )
                }
                is Result.Error -> LoadResult.Error(Exception(result.error.toString()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

private fun AdvancedFilters.toDto(): AdvancedSearchFiltersDto =
    AdvancedSearchFiltersDto(
        countryIds = countryIds,
        cityIds = cityIds,
        programmeIds = programmeIds,
        intake = intake?.let { IntakeFilterDto(year = it.year, fromMonth = it.fromMonth, toMonth = it.toMonth) }
    )

private fun AdvancedRanges.toDto(): AdvancedSearchRangesDto =
    AdvancedSearchRangesDto(
        tuitionFee = tuitionFee?.toDto(),
        durationMonths = durationMonths?.toDto()
    )

private fun MinMax.toDto(): MinMaxDto =
    MinMaxDto(min = min, max = max)

private fun AdvancedFlags.toDto(): AdvancedSearchFlagsDto =
    AdvancedSearchFlagsDto(hasScholarship = hasScholarship)

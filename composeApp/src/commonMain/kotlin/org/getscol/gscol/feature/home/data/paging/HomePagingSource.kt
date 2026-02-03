package org.getscol.gscol.feature.home.data.paging

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import kotlinx.coroutines.delay
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.data.api_service.HomeApiService
import org.getscol.gscol.feature.home.data.mapper.toCourses
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.domain.model.CourseRequest
import org.getscol.gscol.feature.home.domain.model.PaginationRequest

class HomePagingSource(
    private val homeApiService: HomeApiService,
    private val isUserLogin: Boolean,
    private val isEligible: String? = "ELIGIBLE_ONLY"
) : PagingSource<String, Course>() {

    override fun getRefreshKey(state: PagingState<String, Course>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Course> {
        return try {
            val cursor = params.key
            delay(2000)
            val courseRequest = CourseRequest(
                pagination = PaginationRequest(cursor = cursor),
                listType = isEligible
            )

            val result = homeApiService.getHomeData(courseRequest, isUserLogin)
            when (result) {
                is Result.Success -> {

                    val courses = result.data.data?.courses.orEmpty()
                    val nextCursor = result.data.data?.pagination?.cursor
                    LoadResult.Page(
                        data = courses.toCourses(),
                        prevKey = null,
                        nextKey = if(cursor == nextCursor) null else cursor
                    )
                }

                is Result.Error -> {
                    LoadResult.Error(Exception(result.error.toString()))
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
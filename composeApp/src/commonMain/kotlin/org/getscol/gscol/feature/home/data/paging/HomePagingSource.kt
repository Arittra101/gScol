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
                        nextKey = nextCursor
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

/*
override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
    val anchorPosition = state.anchorPosition ?: return null //visible last item
    val closestPage = state.closestPageToPosition(anchorPosition)
    return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
}

override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
    return try {
        val page = params.key ?: 1
        delay(2000)
        val result = homeApiService.getHomeData(page, params.loadSize, isUserLogin)
        when (result) {
            is Result.Success -> {
                val courses = if (!isUserLogin) result.data.data?.allCourses.orEmpty()
                else result.data.data?.eligible?.courses.orEmpty()

                val hasNext = result.data.data?.pagination?.hasNext ?: false

                LoadResult.Page(
                    data = courses.toCourses(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (hasNext) page+1 else null
                )
            }

            is Result.Error -> {
                LoadResult.Error(
                    Exception(result.error.toString())
                )
            }
        }

    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}*/

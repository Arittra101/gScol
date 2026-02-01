package org.getscol.gscol.home.data.paging

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import kotlinx.coroutines.delay
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.home.data.api_service.HomeApiService
import org.getscol.gscol.home.data.mapper.toCourses
import org.getscol.gscol.home.domain.model.Course

class HomePagingSource(
    private val homeApiService: HomeApiService,
    private val isUserLogin: Boolean
) : PagingSource<Int, Course>() {

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
    }
}
package org.getscol.gscol.home.data.paging

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import org.getscol.gscol.auth.data.AuthTokenProvider
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.home.data.api_service.HomeApiService
import org.getscol.gscol.home.data.mapper.toCourses
import org.getscol.gscol.home.domain.model.Course

class HomePagingSource(
    private val homeApiService: HomeApiService,
    private val authProvider: AuthTokenProvider
) : PagingSource<Int, Course>() {

    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        val anchorPosition = state.anchorPosition ?: return null //visible last item
        val closestPage = state.closestPageToPosition(anchorPosition)
        return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        return try {
            val isUserLogin = authProvider.getAccessToken() != null
            val page = params.key ?: 0
            val result = homeApiService.getHomeData(page, params.loadSize, isUserLogin)

            when (result) {
                is Result.Success -> {
                    val courses= result.data.data?.allCourses.orEmpty()
                    LoadResult.Page(
                        data = courses.toCourses(),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (courses.isEmpty()) null else page + 1
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
package org.getscol.gscol.feature.home.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.feature.auth.data.AuthTokenProvider
import org.getscol.gscol.feature.home.data.api_service.HomeApiService
import org.getscol.gscol.feature.home.data.paging.HomePagingSource
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val homeApiService: HomeApiService,
) : HomeRepository {

    override suspend fun getHomeCoursesStream(isLoggedIn: Boolean): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                enablePlaceholders = false,
                prefetchDistance = 4
            ),
            pagingSourceFactory = { HomePagingSource(homeApiService, isLoggedIn) }
        ).flow
    }
}
package org.getscol.gscol.home.domain.repository

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.home.domain.model.Course

interface HomeRepository {
    suspend fun getHomeCoursesStream(isLoggedIn: Boolean): Flow<PagingData<Course>>
}
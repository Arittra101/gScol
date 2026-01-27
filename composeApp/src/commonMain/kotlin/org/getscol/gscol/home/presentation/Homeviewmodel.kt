package org.getscol.gscol.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.cash.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.home.domain.model.Course
import org.getscol.gscol.home.domain.repository.HomeRepository

@OptIn(ExperimentalCoroutinesApi::class)
class Homeviewmodel(private val homeRepository: HomeRepository, session: Session) : ViewModel() {

    val courses: Flow<PagingData<Course>> = session.isUserLoggedIn
        .flatMapLatest { isLoggedIn ->
            homeRepository.getHomeCoursesStream()
        }
        .cachedIn(viewModelScope)

}
package org.getscol.gscol.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.cash.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.home.domain.model.Course
import org.getscol.gscol.home.domain.repository.HomeRepository

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewmodel(private val homeRepository: HomeRepository, private val session: Session) : ViewModel() {

    init {
        println("HomeVIewmodel")
    }

    val courses: Flow<PagingData<Course>> = session.isUserLoggedIn
        .distinctUntilChanged()
        .flatMapLatest { isLoggedIn ->
            println("Trigger")
            homeRepository.getHomeCoursesStream(isLoggedIn)
        }
        .cachedIn(viewModelScope)

    fun setLoginState(){
        viewModelScope.launch {
            session.setUserLoggedIn(true)
        }
    }

    fun onAction(action: HomeAction) {
        when(action){
            is HomeAction.Change -> {
                setLoginState()
            }
        }
    }

}

sealed interface HomeAction{
    data object Change: HomeAction
}

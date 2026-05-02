package org.getscol.gscol.feature.application.presentation.application_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.model.response.ApplicationInfo

class ApplicationListViewmodel(private val repository: ApplicationRepository) : ViewModel() {

    private val _applicationListUiState = MutableStateFlow(ApplicationListUiState())
    val applicationListUiState: StateFlow<ApplicationListUiState> = _applicationListUiState

    private val _applicationListUiEffect = Channel<ApplicationListUiEffect>(Channel.BUFFERED)
    val applicationListUiEffect = _applicationListUiEffect.receiveAsFlow()

    init {
        getApplicationList()
    }

    private fun getApplicationList(isRefresh: Boolean = false) {
        viewModelScope.launch {
            repository.getApplicationList()
                .onStart {
                    _applicationListUiState.value = if (isRefresh) {
                        _applicationListUiState.value.copy(isRefreshing = true)
                    } else ApplicationListUiState(
                        isLoading = true,
                    )
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _applicationListUiState.value = ApplicationListUiState(
                                isLoading = false,
                                isRefreshing = false,
                                applications = result.data.applications
                            )
                        }

                        is Result.Error -> {
                            _applicationListUiState.value = ApplicationListUiState(
                                isLoading = false,
                                isRefreshing = false,
                            )
                        }
                    }
                }
        }
    }

    private fun refresh() {
        getApplicationList(true)
    }

    fun onAction(action: ApplicationListAction) {
        when (action) {
            is ApplicationListAction.OnClickApplication -> {
                viewModelScope.launch {
                    _applicationListUiEffect.send(
                        ApplicationListUiEffect.NavigateToApplicationScreen(action.applicationId)
                    )
                }
            }

            ApplicationListAction.OnRefreshApplicationList -> {
                refresh()
            }
        }
    }

}

data class ApplicationListUiState(
    val applications: List<ApplicationInfo>? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)

sealed interface ApplicationListUiEffect {
    data class NavigateToApplicationScreen(val applicationId: String) : ApplicationListUiEffect
}

sealed interface ApplicationListAction {
    data object OnRefreshApplicationList : ApplicationListAction
    data class OnClickApplication(val applicationId: String) : ApplicationListAction
}
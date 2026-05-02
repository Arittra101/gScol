package org.getscol.gscol.feature.application.presentation.application_status_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.model.response.ProgressItems
import org.getscol.gscol.feature.application.domain.model.response.StageState

class ApplicationStatusViewmodel(
    private val applicationId: String = "",
    private val applicationRepository: ApplicationRepository
) : ViewModel() {

    private val _applicationState = MutableStateFlow(ApplicationUiState())
    val applicationState: StateFlow<ApplicationUiState?> = _applicationState


    init {
        getApplicationStatus()
    }

    private fun getApplicationStatus() {
        viewModelScope.launch {
            applicationRepository.getApplicationStatus(applicationId)
                .onStart { _applicationState.value = ApplicationUiState(isLoading = true) }
                .collect { result ->
                    _applicationState.value = when (result) {
                        is Result.Success -> {
                            ApplicationUiState(
                                progressPercentage = result.data.getProgressPercentage(),
                                applicationProgressItem = result.data.progressItems.orEmpty(),
                                nestProgressItem = result.data.progressItems?.find { it
                                    .state == StageState.UPCOMING
                                } ?: ProgressItems(),
                                isLoading = false
                            )
                        }
                        is Result.Error -> {
                            ApplicationUiState(isLoading = false)
                        }
                    }

                }
        }
    }

}

data class ApplicationUiState(
    val progressPercentage: String? = null,
    val applicationProgressItem: List<ProgressItems>? = null,
    val nestProgressItem: ProgressItems? = null,
    val isLoading: Boolean = true
)
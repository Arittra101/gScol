package org.getscol.gscol.feature.consultant.presentation.consultant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.feature.consultant.domain.repository.ConsultantRepository

class ConsultantViewModel(
    private val repository: ConsultantRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ConsultantState(isLoading = true))
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ConsultantUiEffect>(replay = 0)
    val uiEffect: SharedFlow<ConsultantUiEffect> = _uiEffect.asSharedFlow()

    init {
        load()
    }

    fun onAction(action: ConsultantAction) {
        when (action) {
            ConsultantAction.Retry -> load()
            is ConsultantAction.SelectConsultant -> {
                viewModelScope.launch {
                    _uiEffect.emit(ConsultantUiEffect.NavigateToDetails(action.consultantId))
                }
            }
            is ConsultantAction.BookSession -> {
                viewModelScope.launch {
                    val url = _state.value.items
                        .firstOrNull { it.id == action.consultantId }
                        ?.bookingUrl
                        ?.trim()
                        .orEmpty()
                    if (url.isNotEmpty()) {
                        _uiEffect.emit(ConsultantUiEffect.OpenBookingUrl(url))
                    }
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.getConsultants()
            result.fold(
                onSuccess = { items ->
                    _state.update { it.copy(isLoading = false, items = items, errorMessage = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Failed to load consultants",
                        )
                    }
                }
            )
        }
    }
}


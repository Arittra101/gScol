package org.getscol.gscol.feature.consultant.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.feature.consultant.domain.repository.ConsultantRepository

class ConsultantDetailsViewModel(
    private val consultantId: String,
    private val repository: ConsultantRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ConsultantDetailsState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.getConsultantById(consultantId)
            result.fold(
                onSuccess = { consultant ->
                    if (consultant == null) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                consultant = null,
                                errorMessage = "Consultant not found",
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                consultant = consultant,
                                errorMessage = null,
                            )
                        }
                    }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            consultant = null,
                            errorMessage = e.message ?: "Failed to load consultant",
                        )
                    }
                }
            )
        }
    }
}


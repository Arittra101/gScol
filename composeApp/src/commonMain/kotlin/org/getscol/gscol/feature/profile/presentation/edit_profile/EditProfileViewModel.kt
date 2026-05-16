package org.getscol.gscol.feature.profile.presentation.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.domain.model.EditProfile
import org.getscol.gscol.feature.profile.domain.repository.EditProfileRepository

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModel(
    private val repository: EditProfileRepository,
) : ViewModel() {

    private val fetchTrigger = MutableStateFlow(Unit)
    private val _uiState = MutableStateFlow(EditProfileUiState(isLoading = true))
    val state: StateFlow<EditProfileUiState> = _uiState

    private val openUrlChannel = Channel<String>(Channel.BUFFERED)
    val openUrlEvents: Flow<String> = openUrlChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            fetchTrigger.flatMapLatest { repository.fetchEditProfile() }
                .collect { result ->
                    _uiState.update { current ->
                        when (result) {
                            is Result.Success -> current.copy(
                                isLoading = false,
                                profile = result.data,
                                errorMessage = null,
                            )
                            is Result.Error -> current.copy(
                                isLoading = false,
                                errorMessage = "Something went wrong. Please try later.",
                            )
                        }
                    }
                }
        }
    }

    fun refresh() {
        fetchTrigger.value = Unit
    }

    fun onViewDocumentClick(documentId: String) {
        val current = _uiState.value
        if (documentId in current.loadingDocumentIds) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingDocumentIds = it.loadingDocumentIds + documentId,
                    documentErrors = it.documentErrors - documentId,
                )
            }
            try {
                when (val result = repository.getDocumentDownloadUrl(documentId)) {
                    is Result.Success -> openUrlChannel.send(result.data)
                    is Result.Error -> _uiState.update {
                        it.copy(
                            documentErrors = it.documentErrors + (documentId to "Could not open document. Tap Retry."),
                        )
                    }
                }
            } finally {
                _uiState.update {
                    it.copy(loadingDocumentIds = it.loadingDocumentIds - documentId)
                }
            }
        }
    }
}

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val profile: EditProfile? = null,
    val errorMessage: String? = null,
    val loadingDocumentIds: Set<String> = emptySet(),
    val documentErrors: Map<String, String> = emptyMap(),
)

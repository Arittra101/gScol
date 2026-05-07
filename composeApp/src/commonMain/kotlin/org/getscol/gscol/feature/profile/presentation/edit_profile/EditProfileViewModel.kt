package org.getscol.gscol.feature.profile.presentation.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.domain.download.DocumentDownloadManager
import org.getscol.gscol.feature.profile.domain.download.DownloadState
import org.getscol.gscol.feature.profile.domain.model.EditProfile
import org.getscol.gscol.feature.profile.domain.repository.EditProfileRepository

class EditProfileViewModel(
    private val repository: EditProfileRepository,
    private val downloadManager: DocumentDownloadManager,
) : ViewModel() {

    private val fetchTrigger = MutableStateFlow(Unit)

    private val profileResultFlow = fetchTrigger.flatMapLatest { repository.fetchEditProfile() }

    val state: StateFlow<EditProfileUiState> = combine(
        profileResultFlow,
        downloadManager.observeAll(),
    ) { result, downloadStates ->
        when (result) {
            is Result.Success -> EditProfileUiState(
                isLoading = false,
                profile = result.data,
                downloadStates = downloadStates,
            )
            is Result.Error -> EditProfileUiState(
                isLoading = false,
                errorMessage = "Something went wrong. Please try later.",
                downloadStates = downloadStates,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EditProfileUiState(isLoading = true),
    )

    fun refresh() {
        fetchTrigger.value = Unit
    }

    fun onDownloadClick(documentId: String) {
        downloadManager.enqueue(documentId)
    }

    fun onCancelDownload(documentId: String) {
        downloadManager.cancel(documentId)
    }

    // ViewModel destruction does NOT cancel the manager — downloads continue while the process lives.
}

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val profile: EditProfile? = null,
    val errorMessage: String? = null,
    val downloadStates: Map<String, DownloadState> = emptyMap(),
)

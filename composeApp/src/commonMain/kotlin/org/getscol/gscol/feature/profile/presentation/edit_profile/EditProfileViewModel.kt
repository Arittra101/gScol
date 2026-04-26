package org.getscol.gscol.feature.profile.presentation.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.domain.model.EditProfile
import org.getscol.gscol.feature.profile.domain.repository.EditProfileRepository

class EditProfileViewModel(
    private val repository: EditProfileRepository,
) : ViewModel() {

    private val fetchTrigger = MutableStateFlow(Unit)

    val state: StateFlow<EditProfileUiState> = fetchTrigger
        .flatMapLatest { repository.fetchEditProfile() }
        .map { result ->
            when (result) {
                is Result.Success -> EditProfileUiState(isLoading = false, profile = result.data)
                is Result.Error -> EditProfileUiState(
                    isLoading = false,
                    profile = null,
                    errorMessage = "Something went wrong. Please try later.",
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = EditProfileUiState(isLoading = true),
        )

    fun refresh() {
        fetchTrigger.value = Unit
    }
}

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val profile: EditProfile? = null,
    val errorMessage: String? = null,
)


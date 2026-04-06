package org.getscol.gscol.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.session.Session

class ProfileViewmodel(
    val session: Session
) : ViewModel() {

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnResetPref -> {
                resetPref()
            }
        }
    }

    private fun resetPref() {
        viewModelScope.launch {
            session.resetUserPref()
        }
    }
}

sealed interface ProfileAction {
    data object OnResetPref : ProfileAction
}
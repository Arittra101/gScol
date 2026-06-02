package org.getscol.gscol.feature.application.presentation.application_details

import org.getscol.gscol.core.presentation.components.PickedFile
import org.getscol.gscol.feature.application.domain.model.response.UploadedDocument

sealed interface ApplicationDetailAction {
    data class OnPickDocumentUpload(val pickedFile: PickedFile, val documentTypeId: String) : ApplicationDetailAction
    data class OnDeleteDocument(val document: UploadedDocument) : ApplicationDetailAction
    data class OnExpand(val documentTypeId: String) : ApplicationDetailAction

    data object OnCancelUpload : ApplicationDetailAction
    data object OnWithdrawApplication: ApplicationDetailAction
    data object OnHideWithdrawBottomSheet: ApplicationDetailAction
    data object OnTrackApplication: ApplicationDetailAction
    data object OnHideDocumentResponseBottomSheet: ApplicationDetailAction
}

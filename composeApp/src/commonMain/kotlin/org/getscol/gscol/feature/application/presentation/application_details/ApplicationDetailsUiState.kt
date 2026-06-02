package org.getscol.gscol.feature.application.presentation.application_details

import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckList

data class ApplicationDetailsUiState(
    val universityCoverImageUrl: String? = null,
    val universityName: String? = null,
    val intakeMonth: String? = null,
    val intakeYear: String? = null,
    val courseName: String? = null,
    val applicationSerialNumber: String? = null,
    val documentCheckLists: List<DocumentCheckList> = listOf(),

    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,

    val showDocumentUploadLoader: Boolean = false,
    val uploadState: UploadState? = null,
    val showConsultantBottomSheet: Boolean = false,

    val showFileUpDownloadBottomSheet: Boolean = false,
    val isFileSuccessResponse: Boolean = false,
    val fileBottomSheetMsg: String = ""
)
package org.getscol.gscol.feature.application.presentation.application_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.feature_components.upload.PdfUploader
import org.getscol.gscol.core.feature_components.upload.domain.model.UploadPdfModel
import org.getscol.gscol.core.presentation.components.PickedFile
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckList
import org.getscol.gscol.feature.application.domain.model.response.UploadedDocument
import org.getscol.gscol.feature.application.presentation.application_details.ApplicationDetailsUiEffect.NavigateToApplicationTracker

class ApplicationDetailsViewmodel(
    private val applicationId: String,
    private val applicationRepository: ApplicationRepository,
    private val pdfUploader: PdfUploader
) : ViewModel() {

    private var uploadJob: Job? = null

    private val _applicationState = MutableStateFlow(ApplicationDetailsUiState())
    val applicationState: StateFlow<ApplicationDetailsUiState> = _applicationState

    private val _applicationDetailsUiEffect = Channel<ApplicationDetailsUiEffect>(Channel.BUFFERED)
    val applicationDetailsUiEffect = _applicationDetailsUiEffect.receiveAsFlow()


    init {
        getApplicationDetails()
    }


    fun getApplicationDetails() {
        viewModelScope.launch {
            applicationRepository.getApplicationById(applicationId)
                .onStart {
                    _applicationState.value = _applicationState.value.copy(isLoading = true)
                }.collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val data = result.data
                            val hasNoTextFields = listOf(
                                data.universityCoverImageUrl,
                                data.universityName,
                                data.intakeMonth,
                                data.intakeYear,
                                data.courseName,
                                data.applicationSerialNumber
                            ).all { it.isNullOrEmpty() }

                            val isEmpty = hasNoTextFields && data.documentCheckLists.isEmpty()
                            val updatedList = data.documentCheckLists.map {
                                it.copy(
                                    canDocumentUpload = it.canUploadDocument(),
                                    documentUploadErrorMsg = it.documentErrorMsg(),
                                    showDltIcon = it.shouldShowDltIcon()
                                )
                            }

                            _applicationState.value = ApplicationDetailsUiState(
                                universityCoverImageUrl = data.universityCoverImageUrl,
                                universityName = data.universityName,
                                intakeMonth = data.intakeMonth,
                                intakeYear = data.intakeYear,
                                courseName = data.courseName,
                                applicationSerialNumber = data.applicationSerialNumber,
                                documentCheckLists = updatedList,
                                isLoading = false,
                                isEmpty = isEmpty,
                            )
                        }

                        is Result.Error -> {
                            _applicationState.value = ApplicationDetailsUiState(isLoading = false, isEmpty = true)
                        }
                    }
                }
        }
    }

    fun onAction(action: ApplicationDetailAction) {
        when (action) {
            is ApplicationDetailAction.OnDeleteDocument -> {
                deleteDocument(applicationId, action.document)
            }

            is ApplicationDetailAction.OnPickDocumentUpload -> {
                uploadPdf(action.pickedFile, action.documentTypeId)
            }

            ApplicationDetailAction.OnRefreshApplicationInfo -> {

            }

            is ApplicationDetailAction.OnExpand -> {
                val documentCheckLists = _applicationState.value.documentCheckLists
                val updatedDocumentCheckLists = documentCheckLists.map {
                    if (it.documentTypeId == action.documentTypeId) {
                        it.copy(isExpandable = !it.isExpandable)
                    } else {
                        it
                    }
                }
                _applicationState.value =
                    _applicationState.value.copy(documentCheckLists = updatedDocumentCheckLists)
            }

            is ApplicationDetailAction.OnCancelUpload -> {
                _applicationState.value = _applicationState.value.copy(
                    showDocumentUploadLoader = false
                )
                uploadJob?.cancel()
            }

            is ApplicationDetailAction.OnWithdrawApplication -> {
                _applicationState.update { it.copy(showConsultantBottomSheet = true) }
            }

            is ApplicationDetailAction.OnTrackApplication -> {
                viewModelScope.launch {
                    _applicationDetailsUiEffect.send(
                        NavigateToApplicationTracker(
                            applicationId
                        )
                    )
                }
            }

            is ApplicationDetailAction.OnHideWithdrawBottomSheet -> {
                _applicationState.update { it.copy(showConsultantBottomSheet = false) }
            }

            is ApplicationDetailAction.OnHideDocumentResponseBottomSheet -> {
                _applicationState.update { it.copy(showFileUpDownloadBottomSheet = false) }
            }
        }
    }

    private fun deleteDocument(applicationId: String, document: UploadedDocument) {
        viewModelScope.launch {
            val deleteDocument = applicationRepository.deleteDocument(applicationId, document.applicationDocumentId.orEmpty())
            deleteDocument.onStart { _applicationState.update { it.copy(isLoading = true) } }
                .collect { r ->
                    when (r) {
                        is Result.Success -> {
                            _applicationState.update { state ->
                                val updated = state.documentCheckLists.map { doc ->
                                    if (doc.documentTypeId == document.documentTypeId) {
                                        doc.copy(
                                            uploadedDocuments = doc.uploadedDocuments.filter
                                            { it.applicationDocumentId != document.applicationDocumentId }
                                        )
                                    } else doc
                                }

                                state.copy(
                                    documentCheckLists = updated, isLoading = false,
                                    isFileSuccessResponse = true,
                                    fileBottomSheetMsg = "File Delete Successfully!",
                                    showFileUpDownloadBottomSheet = true
                                )
                            }
                        }

                        is Result.Error -> {
                            _applicationState.update {
                                it.copy(
                                    isLoading = false,
                                    isFileSuccessResponse = false,
                                    fileBottomSheetMsg = "File Delete Failed!",
                                    showFileUpDownloadBottomSheet = true
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun uploadPdf(pickedFile: PickedFile, documentTypeId: String) {
        uploadJob?.cancel()

        val uploadPdfModel = UploadPdfModel(
            documentTypeId = documentTypeId,
            applicationId = applicationId,
            pickedFile = pickedFile
        )

        uploadJob = viewModelScope.launch {
            _applicationState.value = _applicationState.value.copy(
                showDocumentUploadLoader = true,
                uploadState = UploadState(
                    fileName = pickedFile.fileName,
                    fileSize = (pickedFile.fileByteSize / (1024.0 * 1024.0)).toString(),
                    progress = 0f
                )
            )

            val result = pdfUploader.uploadPdf(uploadPdfModel) {
                _applicationState.value = _applicationState.value.copy(
                    uploadState = _applicationState.value.uploadState?.copy(progress = it)
                )
            }

            when (result) {
                is Result.Success -> {

                    val data = result.data
                    _applicationState.update { state ->
                        val updated = state.documentCheckLists.map { doc ->
                            if (doc.documentTypeId == data.documentTypeId) {
                                doc.copy(
                                    uploadedDocuments =
                                        doc.uploadedDocuments + UploadedDocument(
                                            fileName = data.documentName,
                                            applicationDocumentId = data.documentId,
                                            documentTypeId = data.documentTypeId
                                        )
                                )
                            } else doc
                        }

                        state.copy(
                            documentCheckLists = updated, showDocumentUploadLoader = false,
                            isFileSuccessResponse = true,
                            showFileUpDownloadBottomSheet = true,
                            fileBottomSheetMsg = "Document Upload Successfully!"
                        )
                    }

                }

                is Result.Error -> {
                    _applicationState.value = _applicationState.value.copy(
                        showDocumentUploadLoader = false,
                        isFileSuccessResponse = false,
                        showFileUpDownloadBottomSheet = true,
                        fileBottomSheetMsg = "Document Upload Failed"
                    )
                }

            }
        }
    }

    override fun onCleared() {
        uploadJob?.cancel()
    }
}


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

data class UploadState(
    val fileName: String,
    val fileSize: String,
    val progress: Float
)

sealed interface ApplicationDetailsUiEffect {
    data class NavigateToApplicationTracker(val applicationId: String) : ApplicationDetailsUiEffect
}

sealed interface ApplicationDetailAction {
    data class OnPickDocumentUpload(val pickedFile: PickedFile, val documentTypeId: String) : ApplicationDetailAction
    data class OnDeleteDocument(val document: UploadedDocument) : ApplicationDetailAction
    data class OnExpand(val documentTypeId: String) : ApplicationDetailAction

    data object OnCancelUpload : ApplicationDetailAction
    data object OnRefreshApplicationInfo : ApplicationDetailAction
    data object OnWithdrawApplication: ApplicationDetailAction
    data object OnHideWithdrawBottomSheet: ApplicationDetailAction
    data object OnTrackApplication: ApplicationDetailAction
    data object OnHideDocumentResponseBottomSheet: ApplicationDetailAction
}

package org.getscol.gscol.feature.application.domain.model.response

import org.getscol.gscol.core.helper.orFalse
import org.getscol.gscol.feature.application.presentation.DocumentCategoryState

data class ApplicationDetails(
    val universityCoverImageUrl: String? = null,
    val universityName: String? = null,
    val intakeMonth: String? = null,
    val intakeYear: String? = null,
    val courseName: String? = null,
    val applicationSerialNumber: String? = null,
    val documentCheckLists: List<DocumentCheckList> = emptyList(),
)

data class DocumentCheckList(
    val documentTypeId: String? = null,
    val documentTypeName: String? = null,
    val isRequired: Boolean? = null,
    val isMultipleAllowed: Boolean? = null,
    val overallStatus: DocumentCategoryState,
    val allowedMimeTypes: List<String> = emptyList(),
    val maxFileSizeBytes: Long? = null,
    val uploadedDocuments: List<UploadedDocument> = emptyList(),
    var isExpandable: Boolean = false,
    var canDocumentUpload: Boolean = true,
    var isShowExpandIcon: Boolean= true,
    var documentUploadErrorMsg: String = "",
    var showDltIcon: Boolean = true
) {
    fun canUploadDocument(): Boolean {
        val pendingState = overallStatus.name == DocumentCategoryState.PENDING.name
        val canAddMore = isMultipleAllowed.orFalse() || uploadedDocuments.isEmpty()
        return pendingState && canAddMore
    }

    fun documentErrorMsg(): String {
        return if (overallStatus.name != DocumentCategoryState.PENDING.name) {
            "Document is in verification process, you can not upload."
        } else if (!canUploadDocument()) {
            "Multiple documents not allowed."
        } else {
            ""
        }
    }

    fun shouldShowDltIcon() = (overallStatus.name == DocumentCategoryState.PENDING.name)
}

data class UploadedDocument(
    val applicationDocumentId: String? = null,
    val fileName: String? = null,
    val overallStatus: String? = null,
)
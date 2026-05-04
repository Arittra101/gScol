package org.getscol.gscol.feature.application.domain.model.response

import org.getscol.gscol.core.helper.orFalse

data class ApplicationDetails(
    val universityCoverImageUrl: String? = null,
    val universityName: String? = null,
    val intakeMonth: String? = null,
    val intakeYear: String? = null,
    val courseName: String? = null,
    val applicationSerialNumber: String? = null,
    val documentCheckLists: List<DocumentCheckList?>? = null,
)

data class DocumentCheckList(
    val documentTypeId: String? = null,
    val documentTypeName: String? = null,
    val isRequired: Boolean? = null,
    val isMultipleAllowed: Boolean? = null,
    val overallStatus: String? = null,
    val allowedMimeTypes: List<String?>? = null,
    val maxFileSizeBytes: Long? = null,
    private var uploadedDocuments: List<UploadedDocument?>? = null,

    var isExpandable: Boolean = false,
) {
    fun canUploadDocument(): Boolean {
        return isMultipleAllowed.orFalse()
    }

    fun addFileOnList(uploadedDocument: List<UploadedDocument>) {
        uploadedDocuments = uploadedDocument
    }

    fun totalDocumentsSize(): Int {
        return uploadedDocuments?.size ?: 0
    }

    fun getUploadedDocuments(): List<UploadedDocument> {
        return uploadedDocuments?.mapNotNull { it } ?: emptyList()
    }
}

data class DocumentType(
    val documentTypeId: String? = null,
    val documentTypeCode: String? = null,
    val documentTypeName: String? = null,
)

data class UploadedDocument(
    val applicationDocumentId: String? = null,
    val fileName: String? = null,
    val overallStatus: String? = null,
)
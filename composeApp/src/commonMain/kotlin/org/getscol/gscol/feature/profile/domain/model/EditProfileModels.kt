package org.getscol.gscol.feature.profile.domain.model

data class EditProfile(
    val fullName: String,
    val subtitle: String?,
    val joined: String?,
    val imageUrl: String?,
    val sections: List<InfoSection>,
    val academicRecordsTitle: String,
    val academicRecords: List<AcademicRecordItem>,
)

data class InfoSection(
    val sectionTitle: String,
    val isEditable: Boolean,
    val fields: List<InfoField>,
)

data class InfoField(
    val id: String,
    val label: String,
    val value: String,
)

data class AcademicRecordItem(
    val id: String,
    val label: String,
    val type: String,
    val downloadUrl: String?,
    val status: DocumentStatus,
)

enum class DocumentStatus {
    InProgress,
    Rejected,
    Verified,
}


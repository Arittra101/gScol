package org.getscol.gscol.feature.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.domain.BaseResponse

@Serializable
data class EditProfileDtoResponse(
    @SerialName("data") val data: ProfileDataDto? = null,
) : BaseResponse()

@Serializable
data class ProfileDataDto(
    @SerialName("personalInformation") val personalInformation: ProfileSectionDto? = null,
    @SerialName("academicBackground") val academicBackground: ProfileSectionDto? = null,
    @SerialName("englishTestScore") val englishTestScore: ProfileSectionDto? = null,
    @SerialName("contactInformation") val contactInformation: ProfileSectionDto? = null,
    @SerialName("academicRecord") val academicRecord: AcademicRecordSectionDto? = null,
)

@Serializable
data class ProfileSectionDto(
    @SerialName("sectionTitle") val sectionTitle: String? = null,
    @SerialName("isEditable") val isEditable: Boolean? = null,
    @SerialName("joined") val joined: String? = null,
    @SerialName("img_url") val imgUrl: String? = null,
    @SerialName("fields") val fields: List<FieldDto>? = null,
)

@Serializable
data class FieldDto(
    @SerialName("id") val id: String? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("value") val value: kotlinx.serialization.json.JsonElement? = null,
)

@Serializable
data class AcademicRecordSectionDto(
    @SerialName("sectionTitle") val sectionTitle: String? = null,
    @SerialName("isEditable") val isEditable: Boolean? = null,
    @SerialName("items") val items: List<AcademicRecordItemDto>? = null,
)

@Serializable
data class AcademicRecordItemDto(
    @SerialName("documentType") val documentType: DocumentTypeDto? = null,
    @SerialName("uploadedDocuments") val uploadedDocuments: List<UploadedDocumentDto>? = null,
)

@Serializable
data class DocumentTypeDto(
    @SerialName("documentTypeId") val documentTypeId: String? = null,
    @SerialName("documentTypeCode") val documentTypeCode: String? = null,
    @SerialName("documentTypeName") val documentTypeName: String? = null,
)

@Serializable
data class UploadedDocumentDto(
    @SerialName("documentId") val documentId: String? = null,
    @SerialName("fileName") val fileName: String? = null,
    @SerialName("overallStatus") val overallStatus: String? = null,
)

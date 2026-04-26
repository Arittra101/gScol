package org.getscol.gscol.feature.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditProfileDtoResponse(
    @SerialName("student") val student: StudentDto? = null,
)

@Serializable
data class StudentDto(
    @SerialName("personalInformation") val personalInformation: ProfileSectionDto? = null,
    @SerialName("academicBackground") val academicBackground: ProfileSectionDto? = null,
    @SerialName("englishTestScores") val englishTestScores: ProfileSectionDto? = null,
    @SerialName("contactInformation") val contactInformation: ProfileSectionDto? = null,
    @SerialName("academicRecords") val academicRecords: AcademicRecordsDto? = null,
)

@Serializable
data class ProfileSectionDto(
    @SerialName("sectionTitle") val sectionTitle: String? = null,
    @SerialName("joined") val joined: String? = null,
    @SerialName("img_url") val imgUrl: String? = null,
    @SerialName("isEditable") val isEditable: Boolean? = null,
    @SerialName("fields") val fields: List<FieldDto>? = null,
)

@Serializable
data class FieldDto(
    @SerialName("id") val id: String? = null,
    @SerialName("label") val label: String? = null,
    // value can be string/number/null; keep as JsonElement
    @SerialName("value") val value: kotlinx.serialization.json.JsonElement? = null,
)

@Serializable
data class AcademicRecordsDto(
    @SerialName("sectionTitle") val sectionTitle: String? = null,
    @SerialName("items") val items: List<AcademicRecordItemDto>? = null,
)

@Serializable
data class AcademicRecordItemDto(
    @SerialName("id") val id: String? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("download_url") val downloadUrl: String? = null,
    @SerialName("status") val status: RecordStatusDto? = null,
)

@Serializable
data class RecordStatusDto(
    @SerialName("code") val code: String? = null,
    @SerialName("value") val value: String? = null,
)


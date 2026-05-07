package org.getscol.gscol.feature.profile.data.mapper

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import org.getscol.gscol.feature.profile.data.dto.AcademicRecordItemDto
import org.getscol.gscol.feature.profile.data.dto.EditProfileDtoResponse
import org.getscol.gscol.feature.profile.data.dto.FieldDto
import org.getscol.gscol.feature.profile.data.dto.ProfileSectionDto
import org.getscol.gscol.feature.profile.domain.model.AcademicRecordItem
import org.getscol.gscol.feature.profile.domain.model.DocumentStatus
import org.getscol.gscol.feature.profile.domain.model.EditProfile
import org.getscol.gscol.feature.profile.domain.model.InfoField
import org.getscol.gscol.feature.profile.domain.model.InfoSection

fun EditProfileDtoResponse.toDomain(): EditProfile {
    val profile = data
    val personal = profile?.personalInformation

    val fullName = personal?.fields.orEmpty()
        .firstOrNull { it.label?.equals("Full Name", ignoreCase = true) == true }
        ?.value
        ?.toDisplayString()
        ?.ifBlank { null }
        ?: "Student"

    val joined = personal?.joined
    val imageUrl = personal?.imgUrl

    val sections = buildList {
        profile?.personalInformation?.let { add(it.toSectionDomain()) }
        profile?.academicBackground?.let { add(it.toSectionDomain()) }
        profile?.englishTestScore?.let { add(it.toSectionDomain()) }
        profile?.contactInformation?.let { add(it.toSectionDomain()) }
    }

    val academicRecord = profile?.academicRecord
    return EditProfile(
        fullName = fullName,
        subtitle = "Student",
        joined = joined,
        imageUrl = imageUrl,
        sections = sections,
        academicRecordsTitle = academicRecord?.sectionTitle ?: "Academic Records",
        academicRecords = academicRecord?.items.orEmpty().flatMap { it.toDomain() },
    )
}

private fun ProfileSectionDto.toSectionDomain(): InfoSection {
    return InfoSection(
        sectionTitle = sectionTitle.orEmpty(),
        isEditable = isEditable == true,
        fields = fields.orEmpty().map { it.toDomain() },
    )
}

private fun FieldDto.toDomain(): InfoField {
    return InfoField(
        id = id ?: label.orEmpty(),
        label = label.orEmpty(),
        value = value.toDisplayString().ifBlank { "-" },
    )
}

private fun AcademicRecordItemDto.toDomain(): List<AcademicRecordItem> {
    val typeName = documentType?.documentTypeName.orEmpty()
    val typeCode = documentType?.documentTypeCode.orEmpty()
    return uploadedDocuments.orEmpty().map { doc ->
        val statusCode = doc.overallStatus?.trim().orEmpty().uppercase()
        val status = when (statusCode) {
            "UPLOADED", "UNDER_REVIEW", "IN_PROGRESS" -> DocumentStatus.InProgress
            "REJECTED" -> DocumentStatus.Rejected
            "VERIFIED" -> DocumentStatus.Verified
            else -> DocumentStatus.InProgress
        }
        AcademicRecordItem(
            id = doc.documentId.orEmpty(),
            label = typeName,
            type = typeCode,
            status = status,
        )
    }
}

private fun JsonElement?.toDisplayString(): String {
    return when (this) {
        null, JsonNull -> "-"
        is JsonPrimitive -> {
            when {
                isString -> content
                booleanOrNull != null -> boolean.toString()
                intOrNull != null -> int.toString()
                doubleOrNull != null -> {
                    val d = double
                    if (d % 1.0 == 0.0) d.toInt().toString() else d.toString()
                }
                else -> content
            }
        }
        else -> toString()
    }
}

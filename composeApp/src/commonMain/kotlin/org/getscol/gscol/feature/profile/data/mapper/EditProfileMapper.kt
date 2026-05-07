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
    val student = student
    val personal = student?.personalInformation
    val fullName = personal?.fields.orEmpty()
        .firstOrNull { it.id == "full_name" }
        ?.value
        ?.toDisplayString()
        ?.ifBlank { null }
        ?: "Student"

    val joined = personal?.joined
    val imageUrl = personal?.imgUrl

    val sections = buildList {
        student?.personalInformation?.let { add(it.toSectionDomain()) }
        student?.academicBackground?.let { add(it.toSectionDomain()) }
        student?.englishTestScores?.let { add(it.toSectionDomain()) }
        student?.contactInformation?.let { add(it.toSectionDomain()) }
    }

    val academicRecords = student?.academicRecords
    return EditProfile(
        fullName = fullName,
        subtitle = "Student",
        joined = joined,
        imageUrl = imageUrl,
        sections = sections,
        academicRecordsTitle = academicRecords?.sectionTitle ?: "Academic Records",
        academicRecords = academicRecords?.items.orEmpty().map { it.toDomain() },
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
        id = id.orEmpty(),
        label = label.orEmpty(),
        value = value.toDisplayString().ifBlank { "-" },
    )
}

private fun AcademicRecordItemDto.toDomain(): AcademicRecordItem {
    val code = status?.code?.trim().orEmpty().uppercase()
    val mapped = when (code) {
        "UPLOADED", "UNDER_REVIEW", "IN_PROGRESS" -> DocumentStatus.InProgress
        "REJECTED" -> DocumentStatus.Rejected
        "VERIFIED" -> DocumentStatus.Verified
        else -> DocumentStatus.InProgress
    }
    return AcademicRecordItem(
        id = id.orEmpty(),
        label = label.orEmpty(),
        type = type.orEmpty(),
        status = mapped,
    )
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
                    // keep API precision but avoid trailing .0 for ints
                    val d = double
                    if (d % 1.0 == 0.0) d.toInt().toString() else d.toString()
                }

                else -> content
            }
        }

        else -> toString()
    }
}


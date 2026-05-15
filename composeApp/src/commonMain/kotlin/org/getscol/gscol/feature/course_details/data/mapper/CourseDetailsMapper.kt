package org.getscol.gscol.feature.course_details.data.mapper

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.getscol.gscol.core.helper.orFalse
import org.getscol.gscol.feature.course_details.data.dto.AboutUsDto
import org.getscol.gscol.feature.course_details.data.dto.AcademicRequirementsDto
import org.getscol.gscol.feature.course_details.data.dto.CampusLifeDto
import org.getscol.gscol.feature.course_details.data.dto.CourseDetailsDto
import org.getscol.gscol.feature.course_details.data.dto.CourseLocationDto
import org.getscol.gscol.feature.course_details.data.dto.FeesAndScholarshipsDto
import org.getscol.gscol.feature.course_details.data.dto.InfoMetaDataDto
import org.getscol.gscol.feature.course_details.data.dto.IntakeDatesDto
import org.getscol.gscol.feature.course_details.data.dto.UniversityDto
import org.getscol.gscol.feature.course_details.domain.model.AboutUs
import org.getscol.gscol.feature.course_details.domain.model.AcademicRequirements
import org.getscol.gscol.feature.course_details.domain.model.CampusLife
import org.getscol.gscol.feature.course_details.domain.model.Coordinates
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import org.getscol.gscol.feature.course_details.domain.model.CourseLocation
import org.getscol.gscol.feature.course_details.domain.model.CourseRanking
import org.getscol.gscol.feature.course_details.domain.model.CourseTab
import org.getscol.gscol.feature.course_details.domain.model.CourseTag
import org.getscol.gscol.feature.course_details.domain.model.DegreeRequirement
import org.getscol.gscol.feature.course_details.domain.model.EnglishRequirement
import org.getscol.gscol.feature.course_details.domain.model.FeeItems
import org.getscol.gscol.feature.course_details.domain.model.ScholarshipDetails
import org.getscol.gscol.feature.course_details.domain.model.FeesAndScholarships
import org.getscol.gscol.feature.course_details.domain.model.InfoBlock
import org.getscol.gscol.feature.course_details.domain.model.InfoMetaData
import org.getscol.gscol.feature.course_details.domain.model.IntakeDates
import org.getscol.gscol.feature.course_details.domain.model.Requirements
import org.getscol.gscol.feature.course_details.domain.model.TuitionFees
import org.getscol.gscol.feature.course_details.domain.model.University

fun CourseDetailsDto.toDomain(meta: List<InfoMetaDataDto>): CourseDetails {
    return CourseDetails(
        courseId = courseId.orEmpty(),
        courseName = courseName.orEmpty(),
        ranking = ranking?.let {
            CourseRanking(
                position = it.position,
                hasInfo = it.hasInfo ?: false,
                infoKey = it.infoKey,
            )
        },
        university = university.toDomain(),
        tags = tags.orEmpty().mapNotNull { t ->
            val label = t.label?.trim().orEmpty()
            if (label.isBlank()) null else CourseTag(label = label, type = t.type)
        },
        tabs = tabs.orEmpty().mapNotNull { tab ->
            val key = tab.key?.trim().orEmpty()
            val label = tab.label?.trim().orEmpty()
            if (key.isBlank() || label.isBlank()) null else CourseTab(key = key, label = label)
        },
        aboutUs = aboutUs?.toDomain(),
        campusLife = campusLife?.toDomain(),
        location = location?.toDomain(),
        academicRequirements = academicRequirements?.toDomain(),
        feesAndScholarships = feesAndScholarships?.toDomain(),
        intakeDates = intakeDates?.toDomain(),
        isEligible = isEligible.orFalse(),
        isAlreadyApplied = alreadyApplied.orFalse(),
        meta = meta.toDomain(),
    )
}

private fun UniversityDto?.toDomain(): University {
    return University(
        uniId = this?.uniId,
        uniName = this?.uniName.orEmpty(),
        uniLogoUrl = this?.uniLogoUrl,
        uniCoverImageUrl = this?.uniCoverImageUrl,
    )
}

private fun AboutUsDto.toDomain(): AboutUs = AboutUs(description = description.orEmpty())

private fun CampusLifeDto.toDomain(): CampusLife {
    return CampusLife(videoUrls = media?.videoUrl.orEmpty())
}

private fun CourseLocationDto.toDomain(): CourseLocation {
    return CourseLocation(
        city = city,
        country = country,
        state = state,
        address = address,
        coordinates = coordinates?.let {
            Coordinates(
                latitude = it.latitude,
                longitude = it.longitude,
                link = it.link?.trim()?.takeIf { s -> s.isNotEmpty() },
            )
        },
    )
}

private fun AcademicRequirementsDto.toDomain(): AcademicRequirements {
    return AcademicRequirements(
        hasInfo = hasInfo ?: false,
        infoKey = infoKey,
        requirements = requirements?.let { req ->
            Requirements(
                degreeRequirements = req.degreeRequirements.orEmpty().map {
                    DegreeRequirement(
                        degreeName = it.degreeName,
                        label = it.label,
                        minValue = it.minValue,
                    )
                },
                englishRequirements = req.englishRequirements.orEmpty().map {
                    EnglishRequirement(
                        testName = it.testName,
                        minOverallValue = it.minOverallValue,
                        minSectionValue = it.minSectionValue,
                    )
                },
            )
        },
    )
}

private fun FeesAndScholarshipsDto.toDomain(): FeesAndScholarships {
    return FeesAndScholarships(
        hasInfo = hasInfo ?: false,
        infoKey = infoKey,
        items = items?.let { i ->
            val (scholarshipsText, scholarshipDetails) = i.scholarships.parseScholarshipsField()
            FeeItems(
                tuitionFees = i.tuitionFees?.let { tf ->
                    TuitionFees(
                        amount = tf.amount,
                        currency = tf.currency,
                        frequency = tf.frequency,
                    )
                },
                initialDeposit = i.initialDeposit?.trim()?.takeIf { it.isNotEmpty() },
                applicationFee = i.applicationFee?.trim()?.takeIf { it.isNotEmpty() },
                scholarshipsText = scholarshipsText,
                scholarshipDetails = scholarshipDetails,
            )
        },
    )
}

private fun JsonElement?.parseScholarshipsField(): Pair<String?, ScholarshipDetails?> {
    if (this == null) return null to null
    return when (this) {
        is JsonPrimitive -> {
            val text = this.content.trim()
            if (text.isEmpty()) null to null else text to null
        }

        is JsonObject -> {
            fun JsonElement?.stringField(): String? =
                (this as? JsonPrimitive)?.content?.trim()?.takeIf { it.isNotEmpty() }
            null to ScholarshipDetails(
                scholarshipName = this["scholarshipName"].stringField(),
                scholarshipAmount = this["scholarshipAmount"].stringField(),
                currency = this["currency"].stringField(),
                scholarshipType = this["scholarshipType"].stringField(),
            ).takeIf { s ->
                s.scholarshipName != null || s.scholarshipAmount != null ||
                        s.currency != null || s.scholarshipType != null
            }
        }

        else -> null to null
    }
}

private fun IntakeDatesDto.toDomain(): IntakeDates {
    return IntakeDates(
        hasInfo = hasInfo ?: false,
        infoKey = infoKey,
        intakes = intakes.toIntakeList(),
    )
}

private fun JsonElement?.toIntakeList(): List<String> {
    if (this == null) return emptyList()
    return when (this) {
        is JsonArray -> {
            this.mapNotNull { el ->
                val prim = el as? JsonPrimitive ?: return@mapNotNull null
                prim.content.trim().takeIf { it.isNotEmpty() }
            }
        }

        is JsonObject -> {
            this.entries.mapNotNull { (_, value) ->
                val prim = value as? JsonPrimitive ?: return@mapNotNull null
                prim.content.trim().takeIf { it.isNotEmpty() }
            }
        }

        is JsonPrimitive -> {
            val text = this.content.trim()
            if (text.isBlank()) emptyList() else listOf(text)
        }
    }
}

private fun List<InfoMetaDataDto>.toDomain(): List<InfoMetaData> {
    return mapNotNull { m ->
        val key = m.infoKey?.trim().orEmpty()
        val title = m.title?.trim().orEmpty()
        if (key.isBlank() || title.isBlank()) return@mapNotNull null
        InfoMetaData(
            infoKey = key,
            title = title,
            information = m.information.orEmpty().map { b ->
                InfoBlock(
                    subtitle = b.subtitle,
                    description = b.description.orEmpty(),
                )
            },
        )
    }
}


package org.getscol.gscol.feature.academic_form.data.academic_dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto

@Serializable
data class AcademicInfoDtoResponse(
    @SerialName("data")
    val data: ProfileDataDto? = null
) : BaseDto()

@Serializable
data class ProfileDataDto(
    @SerialName("degrees") val degrees: List<DegreeDto>? = null,
    @SerialName("englishTests") val englishTests: List<EnglishTestDto>? = null,
    @SerialName("preferredCountries") val preferredCountries: List<PreferenceDto>? = null,
    @SerialName("preferredPrograms") val preferredPrograms: List<PreferenceDto>? = null
)

@Serializable
data class DegreeDto(
    @SerialName("degreeId") val degreeId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("gpa") val gpa: Double? = null,
    @SerialName("institute") val institute: String? = null,
    @SerialName("passingDate") val passingDate: String? = null,
    @SerialName("validation") val validation: DegreeValidationDto? = null
)

@Serializable
data class DegreeValidationDto(
    @SerialName("gpaScale") val gpaScale: Int? = null
)

@Serializable
data class EnglishTestDto(
    @SerialName("testId") val testId: String? = null,
    @SerialName("testName") val testName: String? = null,
    @SerialName("overallScore") val overallScore: Double? = null,
    @SerialName("testDate") val testDate: String? = null,
    @SerialName("sections") val sections: List<TestSectionDto>? = null,
    @SerialName("validation") val validation: EnglishTestValidationDto? = null
)

@Serializable
data class TestSectionDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("score") val score: Double? = null
)

@Serializable
data class EnglishTestValidationDto(
    @SerialName("maxScore") val maxScore: Int? = null,
    @SerialName("sections") val sections: List<TestSectionValidationDto>? = null
)

@Serializable
data class TestSectionValidationDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("maxScore") val maxScore: Int? = null
)

@Serializable
data class PreferenceDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("selected") val selected: Boolean? = null
)

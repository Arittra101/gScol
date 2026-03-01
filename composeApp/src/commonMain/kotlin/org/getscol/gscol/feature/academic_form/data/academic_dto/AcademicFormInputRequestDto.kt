package org.getscol.gscol.feature.academic_form.data.academic_dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcademicInfoRequest(
    @SerialName("academicResults") val academicResults: List<AcademicResultRequest>? = null,
    @SerialName("lastAcademicInstitute") val lastAcademicInstitute: String? = null,
    @SerialName("englishTestResults") val englishTestResults: List<EnglishTestResultRequest>? = null,
    @SerialName("preferredCountryIds") val preferredCountryIds: List<String>? = null,
    @SerialName("preferredProgrammeIds") val preferredProgrammeIds: List<String>? = null
)

@Serializable
data class AcademicResultRequest(
    @SerialName("degreeId") val degreeId: String? = null,
    @SerialName("gpa") val gpa: Double? = null,
    @SerialName("institute") val institute: String? = null,
    @SerialName("passingDate") val passingDate: String? = null
)

@Serializable
data class EnglishTestResultRequest(
    @SerialName("testId") val testId: String? = null,
    @SerialName("overallScore") val overallScore: Double? = null,
    @SerialName("testDate") val testDate: String? = null,
    @SerialName("sections") val sections: List<EnglishSectionRequest>? = null
)

@Serializable
data class EnglishSectionRequest(
    @SerialName("id") val id: String? = null,
    @SerialName("score") val score: Double? = null
)

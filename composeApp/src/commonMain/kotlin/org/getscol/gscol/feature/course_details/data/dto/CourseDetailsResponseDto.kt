package org.getscol.gscol.feature.course_details.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.getscol.gscol.core.data.dto.BaseDto

/**
 * Backend may return either:
 * - wrapper: { status, message, statusCode, data: { courseDetails, meta } }
 * - or directly: { courseDetails, meta }
 */
@Serializable
data class CourseDetailsResponseDto(
    @SerialName("data") val data: CourseDetailsPayloadDto? = null,
    @SerialName("courseDetails") val courseDetails: CourseDetailsDto? = null,
    @SerialName("meta") val meta: List<InfoMetaDataDto>? = null,
) : BaseDto()

@Serializable
data class CourseDetailsPayloadDto(
    @SerialName("courseDetails") val courseDetails: CourseDetailsDto? = null,
    @SerialName("meta") val meta: List<InfoMetaDataDto>? = null,
)

@Serializable
data class CourseDetailsDto(
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("courseName") val courseName: String? = null,
    @SerialName("ranking") val ranking: CourseRankingDto? = null,
    @SerialName("university") val university: UniversityDto? = null,
    @SerialName("tags") val tags: List<CourseTagDto>? = null,
    @SerialName("tabs") val tabs: List<CourseTabDto>? = null,
    @SerialName("isEligible") val isEligible: Boolean? = null,
    @SerialName("alreadyApplied") val alreadyApplied: Boolean? = null,
    @SerialName("aboutUs") val aboutUs: AboutUsDto? = null,
    @SerialName("campusLife") val campusLife: CampusLifeDto? = null,
    @SerialName("location") val location: CourseLocationDto? = null,
    @SerialName("academicRequirements") val academicRequirements: AcademicRequirementsDto? = null,
    @SerialName("feesAndScholarships") val feesAndScholarships: FeesAndScholarshipsDto? = null,
    @SerialName("intakeDates") val intakeDates: IntakeDatesDto? = null,
)

@Serializable
data class CourseRankingDto(
    @SerialName("position") val position: Int? = null,
    @SerialName("hasInfo") val hasInfo: Boolean? = null,
    @SerialName("infoKey") val infoKey: String? = null,
)

@Serializable
data class UniversityDto(
    @SerialName("uniId") val uniId: String? = null,
    @SerialName("uniName") val uniName: String? = null,
    @SerialName("uniLogoUrl") val uniLogoUrl: String? = null,
    @SerialName("uniCoverImageUrl") val uniCoverImageUrl: String? = null,
)

@Serializable
data class CourseTagDto(
    @SerialName("label") val label: String? = null,
    @SerialName("type") val type: String? = null,
)

@Serializable
data class CourseTabDto(
    @SerialName("key") val key: String? = null,
    @SerialName("label") val label: String? = null,
)

@Serializable
data class AboutUsDto(
    @SerialName("description") val description: List<String>? = null,
)

@Serializable
data class CampusLifeDto(
    @SerialName("media") val media: CampusLifeMediaDto? = null,
)

@Serializable
data class CampusLifeMediaDto(
    @SerialName("videoUrl") val videoUrl: List<String>? = null,
)

@Serializable
data class CourseLocationDto(
    @SerialName("city") val city: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("state") val state: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("coordinates") val coordinates: CoordinatesDto? = null,
)

@Serializable
data class CoordinatesDto(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("link") val link: String? = null,
)

@Serializable
data class AcademicRequirementsDto(
    @SerialName("hasInfo") val hasInfo: Boolean? = null,
    @SerialName("infoKey") val infoKey: String? = null,
    @SerialName("requirements") val requirements: RequirementsDto? = null,
)

@Serializable
data class RequirementsDto(
    @SerialName("degreeRequirements") val degreeRequirements: List<DegreeRequirementDto>? = null,
    @SerialName("englishRequirements") val englishRequirements: List<EnglishRequirementDto>? = null,
)

@Serializable
data class DegreeRequirementDto(
    @SerialName("degreeName") val degreeName: String? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("minValue") val minValue: String? = null,
)

@Serializable
data class EnglishRequirementDto(
    @SerialName("testName") val testName: String? = null,
    @SerialName("minOverallValue") val minOverallValue: String? = null,
    @SerialName("minSectionValue") val minSectionValue: String? = null,
)

@Serializable
data class FeesAndScholarshipsDto(
    @SerialName("hasInfo") val hasInfo: Boolean? = null,
    @SerialName("infoKey") val infoKey: String? = null,
    @SerialName("items") val items: FeeItemsDto? = null,
)

@Serializable
data class FeeItemsDto(
    @SerialName("tuitionFees") val tuitionFees: TuitionFeesDto? = null,
    @SerialName("initialDeposit") val initialDeposit: String? = null,
    @SerialName("applicationFee") val applicationFee: String? = null,
    /** Plain string or structured object, depending on API version. */
    @SerialName("scholarships") val scholarships: JsonElement? = null,
)

@Serializable
data class TuitionFeesDto(
    @SerialName("amount") val amount: String? = null,
    @SerialName("currency") val currency: String? = null,
    @SerialName("frequency") val frequency: String? = null,
)

@Serializable
data class IntakeDatesDto(
    @SerialName("hasInfo") val hasInfo: Boolean? = null,
    @SerialName("infoKey") val infoKey: String? = null,
    /** Backend may send a JSON array of strings or an object (e.g. `{ "fall": "April", "spring": "September" }`). */
    @SerialName("intakes") val intakes: JsonElement? = null,
)

@Serializable
data class InfoMetaDataDto(
    @SerialName("infoKey") val infoKey: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("information") val information: List<InfoBlockDto>? = null,
)

@Serializable
data class InfoBlockDto(
    @SerialName("subtitle") val subtitle: String? = null,
    @SerialName("description") val description: List<String>? = null,
)


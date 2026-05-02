package org.getscol.gscol.feature.course_details.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseDetails(
    val courseId: String,
    val courseName: String,
    val ranking: CourseRanking? = null,
    val university: University,
    val tags: List<CourseTag> = emptyList(),
    val tabs: List<CourseTab> = emptyList(),
    val aboutUs: AboutUs? = null,
    val campusLife: CampusLife? = null,
    val location: CourseLocation? = null,
    val academicRequirements: AcademicRequirements? = null,
    val feesAndScholarships: FeesAndScholarships? = null,
    val intakeDates: IntakeDates? = null,
    val meta: List<InfoMetaData> = emptyList(),
)

data class CampusLifeItem(
    val title: String,
    val thumbnailUrl: String?,
    val duration: String?,
    val count: String?,
    val isVideo: Boolean,
    val videoUrl: String? = null,
    val exploreUrl: String? = null,
)

@Serializable
data class CourseRanking(
    val position: Int? = null,
    val hasInfo: Boolean = false,
    val infoKey: String? = null,
)

@Serializable
data class University(
    val uniId: String? = null,
    val uniName: String,
    val uniLogoUrl: String? = null,
    val uniCoverImageUrl: String? = null,
)

@Serializable
data class CourseTag(
    val label: String,
    val type: String? = null,
)

@Serializable
data class CourseTab(
    val key: String,
    val label: String,
)

@Serializable
data class AboutUs(
    val description: List<String> = emptyList(),
)

@Serializable
data class CampusLife(
    val videoUrls: List<String> = emptyList(),
)

@Serializable
data class CourseLocation(
    val city: String? = null,
    val country: String? = null,
    val state: String? = null,
    val address: String? = null,
    val coordinates: Coordinates? = null,
)

@Serializable
data class Coordinates(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val link: String? = null,
)

@Serializable
data class AcademicRequirements(
    val hasInfo: Boolean = false,
    val infoKey: String? = null,
    val requirements: Requirements? = null,
)

@Serializable
data class Requirements(
    val degreeRequirements: List<DegreeRequirement> = emptyList(),
    val englishRequirements: List<EnglishRequirement> = emptyList(),
)

@Serializable
data class DegreeRequirement(
    val degreeName: String? = null,
    val label: String? = null,
    val minValue: String? = null,
)

@Serializable
data class EnglishRequirement(
    val testName: String? = null,
    val minOverallValue: String? = null,
    val minSectionValue: String? = null,
)

@Serializable
data class FeesAndScholarships(
    val hasInfo: Boolean = false,
    val infoKey: String? = null,
    val items: FeeItems? = null,
)

@Serializable
data class FeeItems(
    val tuitionFees: TuitionFees? = null,
    val initialDeposit: String? = null,
    val applicationFee: String? = null,
    val scholarshipsText: String? = null,
    val scholarshipDetails: ScholarshipDetails? = null,
)

@Serializable
data class ScholarshipDetails(
    val scholarshipName: String? = null,
    val scholarshipAmount: String? = null,
    val currency: String? = null,
    val scholarshipType: String? = null,
)

@Serializable
data class TuitionFees(
    val amount: String? = null,
    val currency: String? = null,
    val frequency: String? = null,
)

@Serializable
data class IntakeDates(
    val hasInfo: Boolean = false,
    val infoKey: String? = null,
    val intakes: List<String> = emptyList(),
)

@Serializable
data class InfoBlock(
    val subtitle: String? = null,
    val description: List<String> = emptyList(),
)

@Serializable
data class InfoMetaData(
    val infoKey: String,
    val title: String,
    val information: List<InfoBlock> = emptyList(),
)

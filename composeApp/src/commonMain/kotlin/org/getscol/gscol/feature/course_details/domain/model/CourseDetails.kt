package org.getscol.gscol.feature.course_details.domain.model

data class CourseDetails(
    val courseId: String,
    val courseName: String,
    val ranking: String?,
    val universityName: String,
    val universityLogoUrl: String,
    val imageUrl: String,
    val establishedYear: String?,
    val institutionType: String,
    val location: String,
    val aboutUs: String,
    val campusLifeVideos: List<CampusLifeItem> = emptyList(),
    val locationMapPlaceholder: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val academicRequirements: List<Pair<String, String>> = emptyList(),
    val feesAndScholarships: List<Pair<String, String>> = emptyList(),
    val intakeDates: List<Pair<String, String>> = emptyList(),
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

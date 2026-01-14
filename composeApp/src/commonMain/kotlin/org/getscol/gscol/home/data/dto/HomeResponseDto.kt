package org.getscol.gscol.home.data.dto

import kotlinx.serialization.SerialName
import org.getscol.gscol.core.data.dto.BaseDto

data class HomeResponseDto(
    @SerialName("data") val data: CourseListDto? = null
) : BaseDto()

data class CourseListDto(
    @SerialName("userState") val userState: String? = null,
    @SerialName("listType") val listType: String? = null,
    @SerialName("pagination") val pagination: PaginationDto? = null,
    @SerialName("all_courses") val allCourses: List<CourseDto>? = null,
    @SerialName("eligible") val eligible: CourseGroupDto? = null,
    @SerialName("ineligible") val ineligible: CourseGroupDto? = null
)

data class PaginationDto(
    @SerialName("page") val page: Int? = null,
    @SerialName("limit") val limit: Int? = null,
    @SerialName("totalItems") val totalItems: Int? = null,
    @SerialName("totalPages") val totalPages: Int? = null,
    @SerialName("hasNext") val hasNext: Boolean? = null
)

data class CourseGroupDto(
    @SerialName("courses") val courses: List<CourseDto>? = null
)

data class CourseDto(
    @SerialName("universityId") val universityId: String? = null,
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("courseName") val courseName: String? = null,
    @SerialName("universityName") val universityName: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("intake") val intake: String? = null,
    @SerialName("tuitionFee") val tuitionFee: Int? = null,
    @SerialName("currency") val currency: String? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("scholarship") val scholarship: Int? = null,
    @SerialName("deposit") val deposit: Int? = null,
    @SerialName("ieltsBandRequired") val ieltsBandRequired: String? = null,
    @SerialName("ieltsOverallRequired") val ieltsOverallRequired: String? = null,
    @SerialName("isWishlisted") val isWishlisted: Boolean? = null
)

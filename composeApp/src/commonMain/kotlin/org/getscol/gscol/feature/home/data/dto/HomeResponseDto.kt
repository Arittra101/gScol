package org.getscol.gscol.feature.home.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseDto

@Serializable
data class HomeResponseDto(
    @SerialName("data") val data: CourseListDto? = null
) : BaseDto()


@Serializable
data class CourseListDto(
    @SerialName("userState") val userState: String? = null,
    @SerialName("academicFormStatus") val academicFormStatus: String? = null,
    @SerialName("listType") val listType: String? = null,
    @SerialName("pagination") val pagination: PaginationDto? = null,
    @SerialName("courses") val courses: List<CourseDto>? = null,
)

@Serializable
data class PaginationDto(
    @SerialName("cursor") val cursor: String? = null,
    @SerialName("hasNext") val hasNext: Boolean? = null,
    @SerialName("limit") val limit: Int? = null,
    @SerialName("page") val page: Int? = null,
)

@Serializable
data class CourseGroupDto(
    @SerialName("courses") val courses: List<CourseDto>? = null
)

@Serializable
data class CourseDto(
    @SerialName("courseId") val courseId: String? = null,
    @SerialName("courseName") val courseName: String? = null,
    @SerialName("university") val university: UniversityDto? = null,
    @SerialName("imgUrl") val imgUrl: String? = null,
    @SerialName("intake") val intake: IntakeDto? = null,
    @SerialName("tuitionFee") val tuitionFee: Int? = null,
    @SerialName("currency") val currency: String? = null,
    @SerialName("durationMonths") val durationMonths: Int? = null,
    @SerialName("initialDeposit") val initialDeposit: Int? = null,
    @SerialName("applicationFee") val applicationFee: Int? = null,
    @SerialName("isScholarshipAvailable") val isScholarshipAvailable: Boolean? = null,
    @SerialName("engRequirements") val engRequirements: List<EngRequirementDto>? = null,
    @SerialName("isWishlisted") val isWishlisted: Boolean? = null
)


@Serializable
data class UniversityDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("state") val state: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("logoUrl") val logoUrl: String? = null,
    @SerialName("imgUrl") val imgUrl: String? = null
)


@Serializable
data class IntakeDto(
    @SerialName("name") val name: String? = null,
    @SerialName("year") val year: Int? = null
)

@Serializable
data class EngRequirementDto(
    @SerialName("testName") val testName: String? = null,
    @SerialName("overall") val overall: Double? = null,
    @SerialName("section") val section: Double? = null
)

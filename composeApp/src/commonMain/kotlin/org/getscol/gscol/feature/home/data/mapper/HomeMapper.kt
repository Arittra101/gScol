package org.getscol.gscol.feature.home.data.mapper

import org.getscol.gscol.core.helper.toDollar
import org.getscol.gscol.feature.home.data.dto.CourseDto
import org.getscol.gscol.feature.home.domain.model.Course

fun CourseDto.toCourse(): Course {
    val ieltsReq = engRequirements?.find { it.testName?.contains("IELTS", ignoreCase = true) == true }
    val universityHero = university?.imgUrl?.takeIf { it.isNotBlank() }
    val heroImage = universityHero.orEmpty()
    val logo = university?.logoUrl?.takeIf { it.isNotBlank() }.orEmpty()

    return Course(
        universityId = university?.id.orEmpty(),
        courseId = courseId.orEmpty(),
        city = university?.city.orEmpty(),
        courseName = courseName.orEmpty(),
        universityName = university?.name.orEmpty(),
        country = university?.country.orEmpty(),
        imageUrl = heroImage,
        universityLogoUrl = logo,
        intake = intake?.name ?: "N/A",
        tuitionFee = tuitionFee?.toString().toDollar(),
        currency = currency.orEmpty(),
        duration = durationMonths?.let { "$it mo" } ?: "N/A",
        scholarship = if (isScholarshipAvailable == true) "Available" else "N/A",
        deposit = initialDeposit?.toString().toDollar(),
        ieltsBandRequired = ieltsReq?.overall.toString(),
        ieltsOverallRequired = ieltsReq?.section.toString(),
        isWishListed = isWishlisted ?: false
    )
}

fun List<CourseDto>.toCourses(): List<Course> {
    return map { it.toCourse() }
}

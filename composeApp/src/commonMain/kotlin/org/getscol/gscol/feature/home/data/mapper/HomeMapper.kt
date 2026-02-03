package org.getscol.gscol.feature.home.data.mapper

import org.getscol.gscol.feature.home.data.dto.CourseDto
import org.getscol.gscol.feature.home.domain.model.Course

fun CourseDto.toCourse(): Course {
    val ieltsReq = engRequirements?.find { it.testName?.contains("IELTS", ignoreCase = true) == true }

    return Course(
        universityId = university?.id.orEmpty(),
        courseId = courseId.orEmpty(),
        city = university?.city.orEmpty(),
        courseName = courseName.orEmpty(),
        universityName = university?.name.orEmpty(),
        country = university?.country.orEmpty(),
        imageUrl = imgUrl.orEmpty(),
        intake = intake?.name.orEmpty(),
        tuitionFee = tuitionFee ?: 0,
        currency = currency.orEmpty(),
        duration = durationMonths?.let { "$it Months" }.orEmpty(),
        scholarship = if (isScholarshipAvailable == true) 1 else 0,
        deposit = initialDeposit ?: 0,
        ieltsBandRequired = ieltsReq?.overall.toString(),
        ieltsOverallRequired = ieltsReq?.section.toString(),
        isWishlisted = isWishlisted ?: false
    )
}
fun List<CourseDto>.toCourses(): List<Course> {
    return map { it.toCourse() }
}

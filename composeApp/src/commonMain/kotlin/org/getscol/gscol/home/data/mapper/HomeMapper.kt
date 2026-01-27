package org.getscol.gscol.home.data.mapper

import org.getscol.gscol.home.data.dto.CourseDto
import org.getscol.gscol.home.domain.model.Course

fun CourseDto.toCourse(): Course {
    return Course(
        universityId = universityId.orEmpty(),
        courseId = courseId.orEmpty(),
        city = city.orEmpty(),
        courseName = courseName.orEmpty(),
        universityName = universityName.orEmpty(),
        country = country.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        intake = intake.orEmpty(),
        tuitionFee = tuitionFee ?: 0,
        currency = currency.orEmpty(),
        duration = duration.orEmpty(),
        scholarship = scholarship ?: 0,
        deposit = deposit ?: 0,
        ieltsBandRequired = ieltsBandRequired.orEmpty(),
        ieltsOverallRequired = ieltsOverallRequired.orEmpty(),
        isWishlisted = isWishlisted ?: false
    )
}

fun List<CourseDto>.toCourses(): List<Course> {
    return map { it.toCourse() }
}

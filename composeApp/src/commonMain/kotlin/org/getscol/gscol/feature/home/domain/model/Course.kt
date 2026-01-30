package org.getscol.gscol.feature.home.domain.model

data class Course(
    val universityId: String,
    val courseId: String,
    val city: String,
    val courseName: String,
    val universityName: String,
    val country: String,
    val imageUrl: String,
    val intake: String,
    val tuitionFee: Int,
    val currency: String,
    val duration: String,
    val scholarship: Int,
    val deposit: Int,
    val ieltsBandRequired: String,
    val ieltsOverallRequired: String,
    val isWishlisted: Boolean
)

package org.getscol.gscol.feature.home.domain.model

data class Course(
    val universityId: String,
    val courseId: String,
    val city: String,
    val courseName: String,
    val universityName: String,
    val country: String,
    val imageUrl: String,
    val universityLogoUrl: String,
    val intake: String,
    val tuitionFee: String,
    val currency: String,
    val duration: String,
    val scholarship: String,
    val deposit: String,
    val ieltsBandRequired: String,
    val ieltsOverallRequired: String,
    val isWishListed: Boolean
)

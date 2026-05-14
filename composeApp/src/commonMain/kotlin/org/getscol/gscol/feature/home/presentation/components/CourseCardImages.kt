package org.getscol.gscol.feature.home.presentation.components

import org.getscol.gscol.feature.home.domain.model.Course

/** Used when API returns no logo / hero image URL. */
const val CourseCardImagePlaceholder =
    "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg"

fun Course.universityLogoForCard(): String =
    universityLogoUrl.ifBlank { CourseCardImagePlaceholder }

fun Course.heroImageForCard(): String =
    imageUrl.ifBlank { CourseCardImagePlaceholder }

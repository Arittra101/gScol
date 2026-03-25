package org.getscol.gscol.feature.course_details.di

import org.getscol.gscol.feature.course_details.presentation.course_details.CourseDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val courseDetailsModule = module {
    viewModelOf(::CourseDetailsViewModel)
}

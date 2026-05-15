package org.getscol.gscol.feature.course_details.di

import org.getscol.gscol.feature.course_details.data.api_service.CourseDetailsApiService
import org.getscol.gscol.feature.course_details.data.api_service.CourseDetailsApiServiceImpl
import org.getscol.gscol.feature.course_details.data.repository.CourseDetailsRepositoryImpl
import org.getscol.gscol.feature.course_details.domain.repository.CourseDetailsRepository
import org.getscol.gscol.feature.course_details.presentation.course_details.CourseDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val courseDetailsModule = module {
    single<CourseDetailsApiService> { CourseDetailsApiServiceImpl(get()) }
    single<CourseDetailsRepository> { CourseDetailsRepositoryImpl(get()) }
    viewModel { params ->
        CourseDetailsViewModel(courseId = params.get(), repository = get(), session = get())
    }
}

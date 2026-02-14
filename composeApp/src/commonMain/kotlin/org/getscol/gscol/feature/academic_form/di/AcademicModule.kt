package org.getscol.gscol.feature.academic_form.di

import org.getscol.gscol.feature.academic_form.data.AcademicRepoImpl
import org.getscol.gscol.feature.academic_form.data.api_service.AcademicApiService
import org.getscol.gscol.feature.academic_form.data.api_service.AcademicApiServiceImpl
import org.getscol.gscol.feature.academic_form.domain.repository.AcademicRepository
import org.getscol.gscol.feature.academic_form.presentation.AcademicViewmodel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val academicModule = module {
    singleOf(::AcademicApiServiceImpl).bind<AcademicApiService>()
    singleOf(::AcademicRepoImpl).bind<AcademicRepository>()
    viewModelOf(::AcademicViewmodel)
}
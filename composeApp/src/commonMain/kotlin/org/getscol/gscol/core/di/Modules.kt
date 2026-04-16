package org.getscol.gscol.core.di

import org.getscol.gscol.auth.di.authModule
import org.getscol.gscol.core.feature_components.upload.di.pdfUploadModule
import org.getscol.gscol.feature.academic_form.di.academicModule
import org.getscol.gscol.feature.application.ApplicationViewmodel
import org.getscol.gscol.feature.course_details.di.courseDetailsModule
import org.getscol.gscol.feature.home.di.homeModule
import org.getscol.gscol.feature.profile.di.profileModule
import org.getscol.gscol.feature.search.di.searchModule
import org.getscol.gscol.feature.search.presentation.search.SearchViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

fun appModules() = listOf(
    platformModule,
    networkModule,
    storageModule,
    authModule,
    sessionModule,
    homeModule,
    searchModule,
    academicModule,
    courseDetailsModule,
    profileModule,
    pdfUploadModule,
    module {  viewModelOf(::ApplicationViewmodel)}
)



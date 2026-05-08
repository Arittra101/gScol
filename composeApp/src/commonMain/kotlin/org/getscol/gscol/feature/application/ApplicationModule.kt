package org.getscol.gscol.feature.application

import org.getscol.gscol.feature.application.data.api_service.ApplicationApiService
import org.getscol.gscol.feature.application.data.api_service.ApplicationApiServiceImp
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.repository.ApplicationRepositoryImp
import org.getscol.gscol.feature.application.presentation.application_details.ApplicationDetailsViewmodel
import org.getscol.gscol.feature.application.presentation.application_form.ApplicationFormViewmodel
import org.getscol.gscol.feature.application.presentation.application_screen.ApplicationListViewmodel
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ApplicationStatusViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {

    single<ApplicationApiService> { ApplicationApiServiceImp(get()) }
    single<ApplicationRepository> { ApplicationRepositoryImp(get()) }

    viewModel { ApplicationListViewmodel(get()) }
    viewModel { params -> ApplicationFormViewmodel(courseDetails = params.get(), get()) }
    viewModel { params -> ApplicationStatusViewmodel(applicationId = params.get(), get()) }
    viewModel { params -> ApplicationDetailsViewmodel(applicationId = params.get(), get(), get()) }
}


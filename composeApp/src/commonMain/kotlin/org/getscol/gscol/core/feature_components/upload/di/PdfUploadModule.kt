package org.getscol.gscol.core.feature_components.upload.di

import org.getscol.gscol.core.feature_components.upload.PdfUploader
import org.getscol.gscol.core.feature_components.upload.data.repository.PdfUploadRepository
import org.getscol.gscol.core.feature_components.upload.data.service.PdfUploaderService
import org.getscol.gscol.core.feature_components.upload.data.service.PdfUploaderServiceImp
import org.getscol.gscol.core.feature_components.upload.domain.PdfUploadRepositoryImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val pdfUploadModule = module {
    singleOf(::PdfUploadRepositoryImp).bind<PdfUploadRepository>()
    singleOf(::PdfUploaderServiceImp).bind<PdfUploaderService>()
    factory { PdfUploader(get()) }
}
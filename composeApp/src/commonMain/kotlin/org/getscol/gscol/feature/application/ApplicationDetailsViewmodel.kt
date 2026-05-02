package org.getscol.gscol.feature.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.getscol.gscol.core.feature_components.upload.PdfUploader
import org.getscol.gscol.core.feature_components.upload.domain.model.UploadPdfModel
import org.getscol.gscol.core.presentation.components.PickedFile
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository

class ApplicationDetailsViewmodel(
    private val applicationId: String,
    private val applicationRepository: ApplicationRepository,
    private val pdfUploader: PdfUploader
) : ViewModel() {

    private var uploadJob: Job? = null

    init {
        println("ApplicationViewmodel ON")
    }

    fun uploadPdf(pickedFile: PickedFile) {
        uploadJob?.cancel()
        val documentId = "2ba09051-f1f4-47e5-b92f-1f920f456081"
        val uploadPdfModel = UploadPdfModel(documentId, pickedFile)

        uploadJob = viewModelScope.launch {
            val isUpload = pdfUploader.uploadPdf(uploadPdfModel) {}
            if (isUpload) {
                println("PDF UPLOAD success")
            } else {
                println("failed")
            }
        }
    }

    override fun onCleared() {
        uploadJob?.cancel()
    }
}


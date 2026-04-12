package org.getscol.gscol.core.feature_components.upload.domain.model

import org.getscol.gscol.core.presentation.components.PickedFile

data class UploadPdfModel(
    val documentTypeId: String,
    val pickedFile : PickedFile,
)
package org.getscol.gscol.feature.application.presentation.application_details

data class UploadState(
    val fileName: String,
    val fileSize: String,
    val progress: Float
)
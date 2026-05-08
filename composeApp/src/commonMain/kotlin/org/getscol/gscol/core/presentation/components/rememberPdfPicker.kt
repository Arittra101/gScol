package org.getscol.gscol.core.presentation.components

import androidx.compose.runtime.Composable

@Composable
fun rememberPdfPicker(onFilePicked: (PickedFile) -> Unit): () -> Unit {
    return rememberFilePicker("application/pdf", onFilePicked)
}
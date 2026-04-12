package org.getscol.gscol.feature.application

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.getscol.gscol.core.presentation.components.rememberFilePicker
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ApplicationScreen(viewmodel: ApplicationViewmodel = koinViewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val pickFile = rememberFilePicker("application/pdf") { pickedFile ->
//            viewModel.uploadDocument(file.bytes, file.name)
            viewmodel.uploadPdf(pickedFile)
//            println("here==>  ${file.fileName}")
        }

        Button(onClick = pickFile) {
            Text("Attach Document")
        }
    }
}

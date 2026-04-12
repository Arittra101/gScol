package org.getscol.gscol.core.presentation.components

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFilePicker(mimeType: String, onResult: (PickedFile) -> Unit): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val bytes = context.contentResolver
            .openInputStream(uri)?.use { it.readBytes() }
            ?: return@rememberLauncherForActivityResult

        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
            null, null, null
        )?.use { cursor ->
            cursor.moveToFirst()
            val name = cursor.getString(0)
            val byteSize = cursor.getLong(1)

            onResult(PickedFile(bytes, name, byteSize, mimeType))
        }
    }

    return remember { { launcher.launch(mimeType) } }
}
/*
SELECT display_name FROM content_provider WHERE uri = ?
*/


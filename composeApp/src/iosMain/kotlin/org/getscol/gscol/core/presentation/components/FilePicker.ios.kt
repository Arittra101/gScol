package org.getscol.gscol.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.*
import platform.UniformTypeIdentifiers.UTTypePDF
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberFilePicker(mimeType: String,onResult: (PickedFile) -> Unit): () -> Unit {
    // remember keeps it alive as long as the composable is in the tree
    val delegate = remember { FilePickerDelegate(mimeType, onResult) }

    return remember {
        {
            val picker = UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(UTTypePDF)
            )
            picker.allowsMultipleSelection = false
            picker.delegate = delegate
            UIApplication.sharedApplication.keyWindow
                ?.rootViewController
                ?.presentViewController(picker, animated = true, completion = null)
        }
    }
}

class FilePickerDelegate(
    private val mimeTypeHint: String,
    private val onResult: (PickedFile) -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? platform.Foundation.NSURL ?: return
        url.startAccessingSecurityScopedResource()
        try {
            val data = NSData.dataWithContentsOfURL(url) ?: return

            // Guard: cannot allocate ByteArray larger than Int.MAX_VALUE
          /*  if (data.length > Int.MAX_VALUE) {
                // too large to handle safely
                return
            }*/

            val bytes = ByteArray(data.length.toInt())
            bytes.usePinned { pinned ->
                platform.posix.memcpy(pinned.addressOf(0), data.bytes, data.length.toULong())
            }

            val fileName = url.lastPathComponent ?: "document.pdf"
            val fileByteSize = data.length.toLong()

            val inferredMime = when (url.pathExtension?.lowercase()) {
                "pdf" -> "application/pdf"
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "txt" -> "text/plain"
                "html", "htm" -> "text/html"
                else -> null
            }

            val finalMimeType = when {
                mimeTypeHint.isNotBlank() -> mimeTypeHint
                inferredMime != null -> inferredMime
                else -> "application/octet-stream"
            }

            onResult(PickedFile(bytes, fileName, fileByteSize, finalMimeType))
        } finally {
            url.stopAccessingSecurityScopedResource()
        }
    }
}
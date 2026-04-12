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
    val delegate = remember { FilePickerDelegate(onResult) }

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
            val bytes = ByteArray(data.length.toInt())
            bytes.usePinned { pinned ->
                platform.posix.memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
            onResult(PickedFile(bytes, url.lastPathComponent ?: "document.pdf"))
        } finally {
            url.stopAccessingSecurityScopedResource()
        }
    }
}
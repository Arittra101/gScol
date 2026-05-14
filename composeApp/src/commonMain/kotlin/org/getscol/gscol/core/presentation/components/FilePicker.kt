package org.getscol.gscol.core.presentation.components

import androidx.compose.runtime.Composable

data class PickedFile(
    val bytes: ByteArray,
    val fileName: String,
    val fileByteSize: Long,
    val mimeType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PickedFile) return false
        return fileName == other.fileName
                && fileByteSize == other.fileByteSize
                && mimeType == other.mimeType
                && bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + fileByteSize.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}

@Composable
expect fun rememberFilePicker(mimeType: String, onResult: (PickedFile) -> Unit): () -> Unit
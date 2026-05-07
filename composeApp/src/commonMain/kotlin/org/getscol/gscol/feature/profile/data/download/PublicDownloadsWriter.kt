package org.getscol.gscol.feature.profile.data.download

import io.ktor.utils.io.ByteReadChannel

expect class PublicDownloadsWriter {
    suspend fun write(
        fileName: String,
        mimeType: String,
        source: ByteReadChannel,
        totalBytes: Long,
        onProgress: (bytesWritten: Long) -> Unit,
    ): String
}

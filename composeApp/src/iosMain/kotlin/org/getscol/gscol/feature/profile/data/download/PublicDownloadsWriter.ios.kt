package org.getscol.gscol.feature.profile.data.download

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSOutputStream
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create

@OptIn(ExperimentalForeignApi::class)
actual class PublicDownloadsWriter {

    actual suspend fun write(
        fileName: String,
        mimeType: String,
        source: ByteReadChannel,
        totalBytes: Long,
        onProgress: (bytesWritten: Long) -> Unit,
    ): String = withContext(Dispatchers.IO) {
        val docDirUrl: NSURL = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        ) ?: error("Cannot access Documents directory")

        val sanitized = sanitize(fileName)
        val fileUrl: NSURL = uniqueUrl(docDirUrl, sanitized)
        val path: String = fileUrl.path ?: error("Cannot get path from URL")

        val outputStream = NSOutputStream.outputStreamToFileAtPath(path, append = false)
        outputStream.open()

        try {
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesWritten = 0L
            while (!source.isClosedForRead) {
                val read = source.readAvailable(buffer)
                if (read > 0) {
                    buffer.usePinned { pinned ->
                        outputStream.write(pinned.addressOf(0), read.toULong())
                    }
                    bytesWritten += read
                    onProgress(bytesWritten)
                }
            }
        } catch (e: Exception) {
            outputStream.close()
            NSFileManager.defaultManager.removeItemAtPath(path, error = null)
            throw e
        }

        outputStream.close()
        path
    }

    private fun sanitize(name: String): String =
        name.replace(Regex("[/\\\\:*?\"<>|]"), "_").trim().ifBlank { "document" }

    private fun uniqueUrl(dir: NSURL, name: String): NSURL {
        val fileManager = NSFileManager.defaultManager
        val dot = name.lastIndexOf('.')
        val base = if (dot >= 0) name.substring(0, dot) else name
        val ext = if (dot >= 0) name.substring(dot) else ""

        var candidate = dir.URLByAppendingPathComponent(name) ?: error("Cannot create file URL")
        var counter = 1
        while (fileManager.fileExistsAtPath(candidate.path ?: "")) {
            candidate = dir.URLByAppendingPathComponent("$base($counter)$ext")
                ?: error("Cannot create file URL")
            counter++
        }
        return candidate
    }
}

package org.getscol.gscol.feature.profile.data.download

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

actual class PublicDownloadsWriter(private val context: Context) {

    actual suspend fun write(
        fileName: String,
        mimeType: String,
        source: ByteReadChannel,
        totalBytes: Long,
        onProgress: (bytesWritten: Long) -> Unit,
    ): String = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeViaMediaStore(fileName, mimeType, source, totalBytes, onProgress)
        } else {
            writeToLegacyDownloads(fileName, source, onProgress)
        }
    }

    private suspend fun writeViaMediaStore(
        fileName: String,
        mimeType: String,
        source: ByteReadChannel,
        totalBytes: Long,
        onProgress: (Long) -> Unit,
    ): String {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, sanitize(fileName))
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: error("MediaStore insert returned null")
        } else {
            error("MediaStore.Downloads is only available on API 29+")
        }

        try {
            resolver.openOutputStream(uri)!!.use { outputStream ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytesWritten = 0L
                while (!source.isClosedForRead) {
                    val read = source.readAvailable(buffer)
                    if (read > 0) {
                        outputStream.write(buffer, 0, read)
                        bytesWritten += read
                        onProgress(bytesWritten)
                    }
                }
            }

            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            throw e
        }

        return uri.toString()
    }

    private suspend fun writeToLegacyDownloads(
        fileName: String,
        source: ByteReadChannel,
        onProgress: (Long) -> Unit,
    ): String {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        downloadsDir.mkdirs()
        val file = uniqueFile(downloadsDir, sanitize(fileName))

        try {
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytesWritten = 0L
                while (!source.isClosedForRead) {
                    val read = source.readAvailable(buffer)
                    if (read > 0) {
                        outputStream.write(buffer, 0, read)
                        bytesWritten += read
                        onProgress(bytesWritten)
                    }
                }
            }
        } catch (e: Exception) {
            file.delete()
            throw e
        }

        return file.absolutePath
    }

    private fun sanitize(name: String): String =
        name.replace(Regex("[\\\\/:*?\"<>|]"), "_").trim().ifBlank { "document" }

    private fun uniqueFile(dir: File, name: String): File {
        var file = File(dir, name)
        if (!file.exists()) return file
        val dot = name.lastIndexOf('.')
        val base = if (dot >= 0) name.substring(0, dot) else name
        val ext = if (dot >= 0) name.substring(dot) else ""
        var counter = 1
        while (file.exists()) {
            file = File(dir, "$base($counter)$ext")
            counter++
        }
        return file
    }
}

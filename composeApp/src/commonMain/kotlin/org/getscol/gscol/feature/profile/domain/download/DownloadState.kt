package org.getscol.gscol.feature.profile.domain.download

sealed class DownloadState {
    data object Idle : DownloadState()
    data object RequestingUrl : DownloadState()
    data class Downloading(
        val progress: Float,
        val bytesDone: Long,
        val totalBytes: Long,
    ) : DownloadState()
    data object Saving : DownloadState()
    data class Completed(val savedPath: String, val mimeType: String) : DownloadState()
    data class Failed(val cause: String) : DownloadState()
    data object Cancelled : DownloadState()
}

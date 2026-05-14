package org.getscol.gscol.feature.profile.domain.download

import kotlinx.coroutines.flow.StateFlow

interface DocumentDownloadManager {
    fun enqueue(documentId: String)
    fun cancel(documentId: String)
    fun observe(documentId: String): StateFlow<DownloadState>
    fun observeAll(): StateFlow<Map<String, DownloadState>>
}

package org.getscol.gscol.feature.common_media

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.getscol.gscol.core.utils.AppLogger

@Composable
actual fun PlatformWebView(
    url: String,
    modifier: Modifier,
    htmlContent: String?,
    onUrlChanged: (String) -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
            }
        },
        update = { webView ->
            when {
                !htmlContent.isNullOrBlank() -> {
                    AppLogger.d("PlatformWebViewAndroid", "Loading HTML content")
                    webView.loadDataWithBaseURL(
                        null,
                        htmlContent,
                        "text/html",
                        "UTF-8",
                        null
                    )
                    onUrlChanged(url)
                }
                else -> if (webView.url != url) {
                    AppLogger.d("PlatformWebViewAndroid", "Loading URL: $url")
                    webView.loadUrl(url)
                    onUrlChanged(url)
                }
            }
        },
    )
}

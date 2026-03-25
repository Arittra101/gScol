package org.getscol.gscol.feature.common_media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(
    url: String,
    modifier: Modifier,
    htmlContent: String?,
    onUrlChanged: (String) -> Unit,
) {
    UIKitView(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration()
            WKWebView(frame = CGRectZero.readValue(), configuration = config)
        },
        update = { webView ->
            when {
                !htmlContent.isNullOrBlank() -> {
                    webView.loadHTMLString(htmlContent!!, null)
                    onUrlChanged(url)
                }
                else -> {
                    webView.loadRequest(NSURLRequest(NSURL(string = url)!!))
                    onUrlChanged(url)
                }
            }
        },
    )
}

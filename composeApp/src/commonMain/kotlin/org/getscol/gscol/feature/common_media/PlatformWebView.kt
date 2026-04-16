package org.getscol.gscol.feature.common_media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformWebView(
    url: String,
    modifier: Modifier,
    htmlContent: String? = null,
    onUrlChanged: (String) -> Unit = {},
)

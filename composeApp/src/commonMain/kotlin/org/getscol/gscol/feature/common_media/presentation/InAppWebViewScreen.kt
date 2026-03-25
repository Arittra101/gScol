package org.getscol.gscol.feature.common_media.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.utils.AppLogger
import org.getscol.gscol.feature.common_media.PlatformWebView
import org.getscol.gscol.navigation.Navigator

@Composable
fun InAppWebViewScreenRoot(
    title: String,
    url: String,
    navigator: Navigator,
    onUrlChanged: (String) -> Unit = {},
) {
    BaseScreen(
        title = title,
        onBackPress = { navigator.navigateBack() },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PlatformWebView(
                url = url,
                modifier = Modifier.fillMaxSize(),
                onUrlChanged = { newUrl ->
                    AppLogger.d("InAppWebView", "URL changed to: $newUrl")
                    onUrlChanged(newUrl)
                },
            )
        }
    }
}

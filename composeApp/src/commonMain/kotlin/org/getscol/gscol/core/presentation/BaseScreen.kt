package org.getscol.gscol.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.getscol.gscol.core.helper.ScolDefaultTopBar
import org.getscol.gscol.core.presentation.components.EmptyView
import org.getscol.gscol.theme.appColors

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    isTopLevelScreen: Boolean = false,
    bgColorContent: Color = appColors().customBackground,
    showBackButton: Boolean = true,
    onBackPress: (() -> Unit)? = null,
    topBar: @Composable (() -> Unit)? = null,
    showLoader: Boolean? = false,
    content: @Composable ((PaddingValues) -> Unit),
) {
    Scaffold(
        topBar = {
            when {
                topBar != null -> topBar()
                title != null -> ScolDefaultTopBar(
                    title = title,
                    onBackPress = onBackPress,
                    isIOSAlignment = isTopLevelScreen,
                    showBackButton = showBackButton
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = bgColorContent)
                .padding(innerPadding)  /*it provide bottom and top system padding*/
                .then(modifier)
        )
        {
            if (showLoader == true) {
                LoadingDialog()
            }
            content(PaddingValues())
        }
    }
}
package org.getscol.gscol.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.utils.HsTopBar
import org.getscol.gscol.core.utils.conditional

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    isTopLevelScreen: Boolean = false,
    zeroBottomPadding: Boolean = false,
    showBackButton: Boolean = true,
    onBackPress: (() -> Unit)? = null,
    topBarContent: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            when {
                topBarContent != null -> topBarContent()
                title != null -> HsTopBar(
                    title = title,
                    showBackButton = showBackButton,
                    onBackPress = onBackPress
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(innerPadding)
                .conditional(
                    isTopLevelScreen = isTopLevelScreen,
                    isZeroBottomPadding = zeroBottomPadding,
                    ifTrue = {
                        Modifier.padding(
                            bottom = innerPadding.calculateBottomPadding() + 56.dp
                        )
                    },

                    ifZeroBottomBarTrue = {
                        Modifier.padding(
                            bottom = 0.dp
                        )
                    },
                )
                .then(modifier)
        ) {
            content(innerPadding)
        }
    }
}
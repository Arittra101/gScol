package org.getscol.gscol.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


inline fun Modifier.conditional(
    isTopLevelScreen: Boolean,
    isZeroBottomPadding: Boolean = false,
    ifTrue: Modifier.() -> Modifier,
    ifZeroBottomBarTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this }
): Modifier {
    return if (isTopLevelScreen) {
        then(ifTrue(Modifier))
    } /*else if (!isZeroBottomPadding) {
        then(ifXMLTrue(Modifier))
    } */else if (isZeroBottomPadding) {
        then(ifZeroBottomBarTrue(Modifier))
    } else {
        then(ifFalse(Modifier))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HsTopBar(
    title: String,
    showBackButton: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color =  MaterialTheme.colorScheme.secondary,
    onBackPress: (() -> Unit)? = null
) {
    val backDispatcher =null

    TopAppBar(
        title = {
            Text(text = title, color = contentColor)
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    onBackPress?.invoke() ?: backDispatcher
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = contentColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor
        ),
        modifier = Modifier.shadow(elevation = 8.dp)
    )
}
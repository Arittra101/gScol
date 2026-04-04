package org.getscol.gscol.core.helper

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors


inline fun Modifier.conditional(
    isTopLevelScreen: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifZeroBottomBarTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this }
): Modifier {
    return if (isTopLevelScreen) {
        then(ifTrue(Modifier))
    }else if (!isTopLevelScreen) {
        then(ifZeroBottomBarTrue(Modifier))
    } else {
        then(ifFalse(Modifier))
    }
}

inline fun Modifier.thenIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier = if (condition) then(modifier()) else this


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScolDefaultTopBar(
    title: String,
    titleColor: Color = Color.Black,
    isIOSAlignment: Boolean? = false,
    fontSize: TextUnit = 18.sp,
    showBackButton: Boolean = true,
    onBackPress: (() -> Unit)? = null,
) {

    TopAppBar(
        title = {
            Text(
                title,
                modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
                color = titleColor,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = if (isIOSAlignment == true) TextAlign.Center else TextAlign.Left
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { onBackPress?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appColors().customSecondaryContainer
        )
    )
}
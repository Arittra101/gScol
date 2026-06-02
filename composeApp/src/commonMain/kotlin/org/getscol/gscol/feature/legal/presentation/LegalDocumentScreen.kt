package org.getscol.gscol.feature.legal.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.navigation.Navigator

@Composable
fun LegalDocumentScreenRoot(
    isTermsAndConditions: Boolean,
    navigator: Navigator,
) {
    val content = LegalContent.forType(isTermsAndConditions)

    BaseScreen(
        title = content.title,
        onBackPress = { navigator.navigateBack() },
    ) { padding ->
        Text(
            text = content.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
        )
    }
}

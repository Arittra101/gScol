package org.getscol.gscol

import androidx.compose.runtime.Composable
import org.getscol.gscol.feature.search.presentation.advance_search.AdvancedSearchScreen
import org.getscol.gscol.feature.search.presentation.advance_search.AdvancedSearchState
import org.getscol.gscol.theme.ScolTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ScolTheme {
       AdvancedSearchScreen(
           state = AdvancedSearchState(),
           onAction = {}
       )
    }
}

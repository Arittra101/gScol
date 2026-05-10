package org.getscol.gscol.feature.consultant.presentation.consultant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.feature.consultant.presentation.components.ConsultantCard
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConsultantScreenRoot(
    navigator: Navigator,
    viewModel: ConsultantViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ConsultantUiEffect.NavigateToDetails -> {
                    navigator.navigateToRoute(Route.ConsultantDetails(effect.consultantId))
                }
            }
        }
    }
    val state by viewModel.state.collectAsState()
    ConsultantScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun ConsultantScreen(
    state: ConsultantState,
    onAction: (ConsultantAction) -> Unit,
) {
    val colors = appColors()
    BaseScreen(
        title = "Consultations",
        isTopLevelScreen = true,
        showLoader = state.isLoading,
        bgColorContent = colors.customSurface,
    ) {

        val navBarPadding = WindowInsets.navigationBars.asPaddingValues()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    bottom = navBarPadding.calculateBottomPadding() + 72.dp
                ),
            ) {
                items(
                    items = state.items,
                    key = { it.id }
                ) { consultant ->
                    ConsultantCard(
                        consultant = consultant,
                        avatarColor = Color(0xFF7C6FCD),
                        onViewProfile = {
                            onAction(
                                ConsultantAction.SelectConsultant(consultant.id)
                            )
                        },
                        onBookSession = {
                            onAction(ConsultantAction.BookSession(consultant.id))
                        }
                    )
                }
            }
        }
    }
}


package org.getscol.gscol.feature.application.presentation.application_status_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.feature.application.domain.model.response.ProgressItems
import org.getscol.gscol.feature.application.presentation.components.NextStepsCard
import org.getscol.gscol.feature.application.presentation.components.StepProgressBar
import org.getscol.gscol.feature.application.presentation.label
import org.getscol.gscol.feature.application.presentation.labelColor
import org.getscol.gscol.feature.application.presentation.progressStateIcon
import org.getscol.gscol.navigation.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import scol.composeapp.generated.resources.Res


val GreenCompleted = Color(0xFF2E7D32)
val GreenActive = Color(0xFF4CAF50)
val GrayPending = Color(0xFFBDBDBD)
val GreyLightInactive = Color(0xFFBDBDBD) // lighter
val CardBackground = Color(0xFFF5F5F5)

val ColorCompleted = Color(0xFF2E7D32)   // dark green
val ColorInProgress = Color(0xFFB71C1C)   // dark red / crimson
val ColorPending = Color(0xFF9E9E9E)   // grey
val ColorBgCard = Color(0xFFF5F5F5)
val ColorNextStepsBg = Color(0xFFFFF3F3)   // very light pink/red tint
val ColorTextPrimary = Color(0xFF212121)
val ColorTextSub = Color(0xFF757575)

@Preview
@Composable
fun ApplicationStatusScreenRoute(
    navigator: Navigator,
    applicationId: String,
    viewmodel: ApplicationStatusViewmodel = koinViewModel(
        key = "ApplicationStatus-$applicationId",
        parameters = { parametersOf(applicationId) })
) {

    val state by viewmodel.applicationState.collectAsState()

    BaseScreen(
        title = "Application Tracker",
        showLoader = state?.isLoading,
        onBackPress = { navigator.navigateBack() }) { paddingValues ->
        ApplicationStatusScreen(paddingValues, state)
    }
}

@Composable
fun ApplicationStatusScreen(paddingValues: PaddingValues, state: ApplicationUiState?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        val applicationProgressList = state?.applicationProgressItem
        val progressPercentage = state?.progressPercentage
        if (applicationProgressList.isNullOrEmpty() || progressPercentage == null) return


        StepProgressBar(state.applicationProgressItem, progressPercentage)

        Spacer(modifier = Modifier.height(28.dp))

        HorizontalDivider(color = CardBackground, thickness = 2.dp)

        Spacer(modifier = Modifier.height(28.dp))

        // ── Step List ───────────────────────────────────────────────────
        applicationProgressList.forEach { step ->
            ApplicationStepItem(progressItems = step)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Next Steps Card ─────────────────────────────────────────────
        NextStepsCard(text = state.nestProgressItem?.stageName)

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun ApplicationStepItem(progressItems: ProgressItems) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorBgCard)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = Res.getUri(progressItems.state?.progressStateIcon() ?: "files/ic_upcoming.svg"),
            contentDescription = "complete stage",
            modifier = Modifier.size(32.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = progressItems.stageName.orEmpty(),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = ColorTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = progressItems.state?.label() ?: "To be complete",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = progressItems.state?.labelColor() ?: ColorPending
            )
        }
    }
}

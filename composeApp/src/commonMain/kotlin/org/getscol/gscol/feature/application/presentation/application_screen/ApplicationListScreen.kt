package org.getscol.gscol.feature.application.presentation.application_screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.helper.ObserveEffect
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.feature.application.domain.model.response.ApplicationInfo
import org.getscol.gscol.feature.application.presentation.components.ApplicationRowItem
import org.getscol.gscol.feature.application.presentation.components.ApplicationRowItemShimmer
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.sqrt

@Composable
fun ApplicationListScreenRoute(
    navigator: Navigator,
    viewmodel: ApplicationListViewmodel = koinViewModel()
) {

    val state by viewmodel.applicationListUiState.collectAsState()
    val action = viewmodel::onAction

    ObserveEffect(viewmodel.applicationListUiEffect) { effect ->
        when (effect) {
            is ApplicationListUiEffect.NavigateToApplicationScreen -> {
                navigator.navigateTo(Route.Application(effect.applicationId))
            }
        }
    }

    BaseScreen(
        title = "Applications",
        isTopLevelScreen = true,
        showBackButton = false,
        onBackPress = { navigator.navigateBack() }) {
        ApplicationTrackerContent(
            state.applications.orEmpty(),
            action,
            state.isRefreshing,
            state.isLoading
        )
    }
}

@Composable
fun ApplicationTrackerContent(
    applications: List<ApplicationInfo>,
    action: (ApplicationListAction) -> Unit,
    isRefreshing: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = { action(ApplicationListAction.OnRefreshApplicationList) },
        indicator = {
            val rawFraction = refreshState.distanceFraction
            val clampedFraction = rawFraction.coerceIn(0f, 1f)
            val isThresholdCrossed = rawFraction >= 1f
            val rubberFraction = sqrt(rawFraction.coerceAtLeast(0f))

            // ✅ Replace BoxWithConstraints — no subcomposition overhead
            var containerHeightPx by remember { mutableStateOf(0) }

            val arrowRotation by animateFloatAsState(
                targetValue = if (isThresholdCrossed) 180f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "ptr_arrow_rotation"
            )

            val scale by animateFloatAsState(
                targetValue = if (isRefreshing) 1f else (0.6f + 0.4f * clampedFraction),
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "ptr_scale"
            )

            val scrimAlpha by animateFloatAsState(
                targetValue = if (isRefreshing) 0.25f else 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "ptr_scrim_alpha"
            )

            val animatedOffsetY by animateFloatAsState(
                // ✅ centerOffset derived directly from measured px — no BoxWithConstraints
                targetValue = if (isRefreshing) {
                    with(LocalDensity.current) { (containerHeightPx / 2f).toDp().value - 20f }
                } else {
                    -48f + 56f * rubberFraction
                },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "ptr_offset"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // ✅ Scrim via drawBehind — draw phase only, no extra composable node
                    .drawBehind {
                        drawRect(color = Color.Black.copy(alpha = scrimAlpha))
                    }
                    // ✅ Captures parent height for centering without subcomposition
                    .onSizeChanged { containerHeightPx = it.height }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(40.dp)
                        .graphicsLayer {
                            translationY = animatedOffsetY.dp.toPx()
                            scaleX = scale
                            scaleY = scale
                            // ✅ Alpha read here in draw phase — no recomposition on finger drag
                            alpha = (clampedFraction / 0.4f).coerceIn(0f, 1f)
                        }
                        .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
                        .clip(CircleShape)
                        .background(Color(0xFF8B3838)),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(
                        targetState = isRefreshing,
                        label = "ptr_content_crossfade"
                    ) { refreshing ->
                        if (refreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDownward,
                                contentDescription = if (isThresholdCrossed) "Release to refresh"
                                else "Pull to refresh",
                                modifier = Modifier
                                    .size(20.dp)
                                    .graphicsLayer { rotationZ = arrowRotation },
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (isLoading) {
                items(3) { ApplicationRowItemShimmer() }
            } else {
                items(
                    items = applications,
                    key = { it.applicationId.orEmpty() }
                ) { application ->
                    ApplicationRowItem(item = application, action = action)
                }
            }
        }
    }
}
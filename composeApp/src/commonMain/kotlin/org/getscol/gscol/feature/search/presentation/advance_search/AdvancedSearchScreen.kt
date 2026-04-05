package org.getscol.gscol.feature.search.presentation.advance_search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.Json
import org.getscol.gscol.core.components.AppDropdown
import org.getscol.gscol.core.components.DropdownOption
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.currentYear
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.getscol.gscol.theme.color.AppColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.search_universities
import kotlin.math.roundToInt

private const val TUITION_MAX = 50_000
private const val DURATION_YEARS_MAX = 5

private fun formatTuitionRangeLabel(maxUsd: Int): String {
    val maxPart =
        if (maxUsd >= TUITION_MAX) "50K+"
        else "${((maxUsd + 500).coerceAtLeast(0)) / 1000}K"
    return "$0K - $maxPart"
}

private fun formatDurationRangeLabel(maxYears: Int): String {
    val y = maxYears.coerceIn(1, DURATION_YEARS_MAX)
    val unit = if (y == 1) "year" else "years"
    return "1 - $y $unit"
}

@Composable
fun AdvancedSearchScreenRoot(
    navigator: Navigator,
    viewModel: AdvancedSearchViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is AdvancedSearchUiEffect.NavigateToResults -> {
                    navigator.navigateToRoute(
                        Route.SearchResults(
                            searchText = effect.searchText,
                            listType = "ELIGIBLE_ONLY",
                            advancedParamsJson = Json.encodeToString(effect.advancedParams)
                        )
                    )
                }
            }
        }
    }
    val state by viewModel.state.collectAsState()
    val onAction = viewModel::onAction

    BaseScreen(
        title = stringResource(Res.string.search_universities),
        showLoader = state.showLoader,
        bgColorContent = appColors().customBackground,
        onBackPress = { navigator.navigateBack() }) {
        AdvancedSearchScreen(state, onAction)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSearchScreen(
    state: AdvancedSearchState,
    onAction: (AdvancedSearchAction) -> Unit,
) {
    val colors = appColors()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            /*  .weight(1f)*/
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AppDropdown(
            selectedOption = state.selectedCountry,
            options = state.countryOptions,
            onOptionSelected = { onAction(AdvancedSearchAction.CountrySelected(it)) },
            placeholder = "Select Country",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Flag
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppDropdown(
            selectedOption = state.selectedCity,
            options = state.cityOptions,
            onOptionSelected = { onAction(AdvancedSearchAction.CitySelected(it)) },
            placeholder = "Select City",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.LocationCity
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppDropdown(
            selectedOption = state.selectedCourse,
            options = state.courseOptions,
            onOptionSelected = { onAction(AdvancedSearchAction.CourseSelected(it)) },
            placeholder = "Course (e.g. Computer Science)",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.CalendarMonth
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Text(
                "Start year & month",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimaryText
            )
            AppDropdown(
                selectedOption = DropdownOption(
                    state.selectedIntakeYear.coerceIn(currentYear(), currentYear() + 5).toString(),
                    state.selectedIntakeYear.coerceIn(currentYear(), currentYear() + 5).toString()
                ),
                options = (currentYear()..currentYear() + 5).map {
                    DropdownOption(
                        it.toString(),
                        it.toString()
                    )
                },
                onOptionSelected = {
                    onAction(
                        AdvancedSearchAction.IntakeYearSelected(
                            it.id.toIntOrNull() ?: state.selectedIntakeYear
                        )
                    )
                },
                placeholder = "Year",
                leadingIcon = Icons.Default.CalendarMonth
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                1 to "Jan",
                2 to "Feb",
                3 to "Mar",
                4 to "Apr",
                5 to "May",
                6 to "Jun"
            ).forEach { (month, label) ->
                IntakeMonthChip(
                    label = label,
                    selected = month in state.selectedIntakeMonths,
                    onClick = { onAction(AdvancedSearchAction.IntakeMonthSelected(month)) },
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                7 to "Jul",
                8 to "Aug",
                9 to "Sep",
                10 to "Oct",
                11 to "Nov",
                12 to "Dec"
            ).forEach { (month, label) ->
                IntakeMonthChip(
                    label = label,
                    selected = month in state.selectedIntakeMonths,
                    onClick = { onAction(AdvancedSearchAction.IntakeMonthSelected(month)) },
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Advanced Filters",
            style = MaterialTheme.typography.titleMedium,
            color = colors.customPrimaryText
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tuition Range",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimaryText
            )
            Text(
                text = formatTuitionRangeLabel(state.tuitionRangeMax),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimary
            )
        }
        val tuitionSliderColors = SliderDefaults.colors(
            thumbColor = colors.customPrimary,
            activeTrackColor = colors.customPrimary,
            inactiveTrackColor = colors.customSecondary
        )
        val tuitionInteractionSource = remember { MutableInteractionSource() }
        Slider(
            value = state.tuitionRangeMax.toFloat(),
            onValueChange = { onAction(AdvancedSearchAction.TuitionRangeChange(it.toInt())) },
            valueRange = 0f..TUITION_MAX.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            colors = tuitionSliderColors,
            interactionSource = tuitionInteractionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = tuitionInteractionSource,
                    colors = tuitionSliderColors,
                    thumbSize = DpSize(24.dp, 24.dp)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors = tuitionSliderColors,
                    enabled = true,
                    thumbTrackGapSize = 0.dp
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Duration",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimaryText
            )
            Text(
                text = formatDurationRangeLabel(state.durationMaxYears),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimary
            )
        }
        val durationSliderColors = SliderDefaults.colors(
            thumbColor = colors.customPrimary,
            activeTrackColor = colors.customPrimary,
            inactiveTrackColor = colors.customSecondary
        )
        val durationInteractionSource = remember { MutableInteractionSource() }
        Slider(
            value = state.durationMaxYears.toFloat(),
            onValueChange = { value ->
                onAction(
                    AdvancedSearchAction.DurationChange(
                        value.roundToInt().coerceIn(1, DURATION_YEARS_MAX)
                    )
                )
            },
            valueRange = 1f..DURATION_YEARS_MAX.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            colors = durationSliderColors,
            interactionSource = durationInteractionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = durationInteractionSource,
                    colors = durationSliderColors,
                    thumbSize = DpSize(24.dp, 24.dp)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors = durationSliderColors,
                    enabled = true,
                    thumbTrackGapSize = 0.dp
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Scholarships",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.customPrimaryText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    onAction(
                        AdvancedSearchAction.ScholarshipFilterChange(
                            if (state.scholarshipFilter == true) null else true
                        )
                    )
                },
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder(),
                modifier = Modifier.weight(1f),
                colors = buttonDefaultsForFilter(state.scholarshipFilter == true, colors)
            ) {
                Text(
                    "Available",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.scholarshipFilter == true) colors.customBackground else colors.customPrimaryText
                )
            }
            Button(
                onClick = {
                    onAction(
                        AdvancedSearchAction.ScholarshipFilterChange(
                            if (state.scholarshipFilter == false) null else false
                        )
                    )
                },
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder(),
                modifier = Modifier.weight(1f),
                colors = buttonDefaultsForFilter(state.scholarshipFilter == false, colors)
            ) {
                Text(
                    "Not Available", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.scholarshipFilter == false) colors.customBackground else colors.customPrimaryText
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onAction(AdvancedSearchAction.Submit) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.customPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            Text(
                "Search",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

}

@Composable
private fun buttonDefaultsForFilter(
    selected: Boolean,
    colors: AppColors
): androidx.compose.material3.ButtonColors =
    ButtonDefaults.buttonColors(
        containerColor = if (selected) colors.customPrimary else colors.customBackground,
        contentColor = if (selected) Color.White else colors.customPrimaryText
    )

@Composable
private fun IntakeMonthChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    colors: AppColors,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) colors.customPrimary.copy(alpha = 0.2f) else colors.customSecondary,
            contentColor = if (selected) colors.customPrimary else colors.customPrimaryText
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

package org.getscol.gscol.feature.search.presentation.advance_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.Json
import org.getscol.gscol.core.components.AppDropdown
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.getscol.gscol.theme.color.AppColors
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

private const val TUITION_MAX = 50_000
private const val DURATION_YEARS_MAX = 5

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
    AdvancedSearchScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = navigator::navigateBack
    )
}

@Composable
fun AdvancedSearchScreen(
    state: AdvancedSearchState,
    onAction: (AdvancedSearchAction) -> Unit,
    onBack: () -> Unit = {}
) {
    val colors = appColors()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.customBackground)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                "Search Universities",
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimaryText,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
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
            Spacer(modifier = Modifier.height(12.dp))
            AppDropdown(
                selectedOption = state.selectedIntake,
                options = state.intakeOptions,
                onOptionSelected = { onAction(AdvancedSearchAction.IntakeSelected(it)) },
                placeholder = "Select Intake",
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.School
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Advanced Filters",
                style = MaterialTheme.typography.titleMedium,
                color = colors.customPrimaryText
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Tuition Range",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.customPrimaryText
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$0",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.customPrimaryText
                )
                Slider(
                    value = state.tuitionRangeMax.toFloat(),
                    onValueChange = { onAction(AdvancedSearchAction.TuitionRangeChange(it.toInt())) },
                    valueRange = 0f..TUITION_MAX.toFloat(),
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = colors.customPrimary,
                        activeTrackColor = colors.customPrimary
                    )
                )
                Text(
                    "\$50k+",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.customPrimaryText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Duration",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.customPrimaryText
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "1 yr",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.customPrimaryText
                )
                Slider(
                    value = state.durationMaxYears.toFloat(),
                    onValueChange = {
                        onAction(
                            AdvancedSearchAction.DurationChange(
                                it.roundToInt().coerceIn(1, DURATION_YEARS_MAX)
                            )
                        )
                    },
                    valueRange = 1f..DURATION_YEARS_MAX.toFloat(),
                    steps = DURATION_YEARS_MAX - 2,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = colors.customPrimary,
                        activeTrackColor = colors.customPrimary
                    )
                )
                Text(
                    "5 yrs+",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.customPrimaryText
                )
            }
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
                        color = colors.customPrimaryText
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
                        color = colors.customPrimaryText
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
            ) {
                Text("Search", color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
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

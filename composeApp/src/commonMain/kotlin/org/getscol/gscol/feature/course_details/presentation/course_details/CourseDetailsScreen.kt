package org.getscol.gscol.feature.course_details.presentation.course_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.KeyValueRow
import org.getscol.gscol.core.presentation.components.PrimaryButton
import org.getscol.gscol.core.presentation.components.ReadMoreText
import org.getscol.gscol.core.presentation.components.ScrollableTabs
import org.getscol.gscol.core.presentation.components.SectionHeader
import org.getscol.gscol.feature.course_details.domain.model.CampusLifeItem
import org.getscol.gscol.feature.course_details.presentation.components.CampusLifeCard
import org.getscol.gscol.feature.course_details.presentation.components.CourseOverviewCard
import org.getscol.gscol.feature.course_details.presentation.components.googleMapsOpenUrl
import org.getscol.gscol.feature.course_details.presentation.components.googleStaticMapImageUrl
import org.getscol.gscol.feature.course_details.presentation.components.LocationMapCard
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt
import com.getscol.gscol.BuildKonfig

private val TAB_TITLES = listOf(
    "About us",
    "Campus Life",
    "Location",
    "Academic Requirements",
    "Fees & Scholarships",
    "Intake Dates",
)

private data class InfoBlock(
    val subtitle: String? = null,
    val description: List<String>,
)

private data class InfoMetaData(
    val infoKey: String,
    val title: String,
    val information: List<InfoBlock>,
)

private val infoMetaDataByKey: Map<String, InfoMetaData> = listOf(
    InfoMetaData(
        infoKey = "rankingMetaData",
        title = "Ranking",
        information = listOf(
            InfoBlock(
                description = listOf(
                    "Ranked #42 globally for International Business Management based on research output, student satisfaction, and graduate employability.",
                    "Consistently ranked in the top 20 universities across the United Kingdom.",
                )
            )
        )
    ),
    InfoMetaData(
        infoKey = "academicRequirementsMetaData",
        title = "Academic Requirements",
        information = listOf(
            InfoBlock(
                subtitle = "GPA",
                description = listOf(
                    "A minimum GPA of 3.5 out of 4.0 is required for consideration into the program."
                )
            ),
            InfoBlock(
                subtitle = "English Proficiency",
                description = listOf(
                    "Non-native English speakers must submit TOEFL scores of 100+ or IELTS scores of 7.0 or above."
                )
            ),
        )
    ),
    InfoMetaData(
        infoKey = "feesAndScholarshipsMetaData",
        title = "Fees & Scholarships",
        information = listOf(
            InfoBlock(
                subtitle = "Tuition Fees",
                description = listOf(
                    "The annual tuition fee is $45,000, covering all core modules and university facilities."
                )
            ),
            InfoBlock(
                subtitle = "Scholarships",
                description = listOf(
                    "Merit-based and need-based scholarships are available. Students can apply during the admissions process."
                )
            ),
        )
    ),
    InfoMetaData(
        infoKey = "intakeDatesMetaData",
        title = "Intake Dates",
        information = listOf(
            InfoBlock(
                subtitle = "Fall Intake",
                description = listOf(
                    "The Fall intake begins in September. Application deadline is typically June 30th."
                )
            ),
            InfoBlock(
                subtitle = "Spring Intake",
                description = listOf(
                    "The Spring intake begins in March. Application deadline is typically December 15th."
                )
            ),
        )
    ),
).associateBy { it.infoKey }

@Composable
fun CourseDetailsScreenRoot(
    courseId: String,
    navigator: Navigator,
    viewModel: CourseDetailsViewModel = koinViewModel(key = "CourseDetails-$courseId"),
) {
    LaunchedEffect(courseId) {
        viewModel.setCourseId(courseId)
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                CourseDetailsUiEffect.NavigateBack -> navigator.navigateBack()
                is CourseDetailsUiEffect.ApplyNow -> {
                    // TODO: Navigate to application flow when ready
                    navigator.navigateBack()
                }
            }
        }
    }
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    CourseDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = navigator::navigateBack,
        onCampusLifeItemClick = { item ->
            when {
                !item.videoUrl.isNullOrBlank() ->
                    navigator.navigateToRoute(
                        Route.CourseVideoPlayer(
                            title = item.title,
                            videoUrl = item.videoUrl
                        )
                    )
            }
        },
        onLocationClick = { lat, lng ->
            uriHandler.openUri(googleMapsOpenUrl(lat, lng))
        },
    )
}

@Composable
fun CourseDetailsScreen(
    state: CourseDetailsState,
    onAction: (CourseDetailsAction) -> Unit,
    onBack: () -> Unit,
    onCampusLifeItemClick: (CampusLifeItem) -> Unit = {},
    onLocationClick: (latitude: Double, longitude: Double) -> Unit = { _, _ -> },
) {
    BaseScreen(
        title = "Course Details",
        onBackPress = onBack,
        showLoader = state.isLoading,
    ) { paddingValues ->
        if (state.isLoading && state.courseDetails == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            return@BaseScreen
        }
        val details = state.courseDetails ?: return@BaseScreen
        val scrollState = rememberScrollState()
        val colors = appColors()
        val selectedInfoMeta = remember { mutableStateOf<InfoMetaData?>(null) }
        val sectionOffsets = remember { mutableStateListOf(0, 0, 0, 0, 0, 0) }
        val tabsEnabled = remember { mutableStateOf(false) }

        LaunchedEffect(state.selectedTabIndex, tabsEnabled.value) {
            if (!tabsEnabled.value) return@LaunchedEffect
            val index = state.selectedTabIndex
            if (index in 0..5) {
                val target = sectionOffsets.getOrNull(index) ?: 0
                scrollState.animateScrollTo(target.coerceAtLeast(0))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(colors.customSurface),
        ) {
            BannerImage(imageUrl = details.imageUrl)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-80).dp),
            ) {
                CourseOverviewCard(
                    courseName = details.courseName,
                    ranking = details.ranking,
                    universityName = details.universityName,
                    universityLogoUrl = details.universityLogoUrl,
                    establishedYear = details.establishedYear,
                    institutionType = details.institutionType,
                    location = details.location,
                    onRankingInfoClick = {
                        selectedInfoMeta.value = infoMetaDataByKey["rankingMetaData"]
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                ScrollableTabs(
                    tabs = TAB_TITLES,
                    selectedIndex = state.selectedTabIndex,
                    onTabSelected = {
                        tabsEnabled.value = true
                        onAction(CourseDetailsAction.TabSelected(it))
                    },
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val y = coords.positionInParent().y.roundToInt()
                        sectionOffsets[0] = y
                    }
                ) {
                    AboutUsSection(aboutUs = details.aboutUs)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val y = coords.positionInParent().y.roundToInt()
                        sectionOffsets[1] = y
                    }
                ) {
                    CampusLifeSection(
                        items = details.campusLifeVideos,
                        onItemClick = onCampusLifeItemClick,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val y = coords.positionInParent().y.roundToInt()
                        sectionOffsets[2] = y
                    }
                ) {
                    LocationSection(
                        latitude = details.latitude,
                        longitude = details.longitude,
                        onLocationClick = onLocationClick,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val y = coords.positionInParent().y.roundToInt()
                        sectionOffsets[3] = y
                    }
                ) {
                    SectionHeader(
                        title = "Academic Requirements",
                        showInfoIcon = true,
                        onInfoClick = {
                            selectedInfoMeta.value = infoMetaDataByKey["academicRequirementsMetaData"]
                        },
                    )
                    details.academicRequirements.forEach { (label, value) ->
                        KeyValueRow(label = label, value = value)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val y = coords.positionInParent().y.roundToInt()
                        sectionOffsets[4] = y
                    }
                ) {
                    SectionHeader(
                        title = "Fees & Scholarships",
                        showInfoIcon = true,
                        onInfoClick = {
                            selectedInfoMeta.value = infoMetaDataByKey["feesAndScholarshipsMetaData"]
                        },
                    )
                    details.feesAndScholarships.forEach { (label, value) ->
                        KeyValueRow(
                            label = label,
                            value = value,
                            valueColor = if (value == "Available") colors.customPrimary else null,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val y = coords.positionInParent().y.roundToInt()
                        sectionOffsets[5] = y
                    }
                ) {
                    SectionHeader(
                        title = "Intake Dates",
                        showInfoIcon = true,
                        onInfoClick = {
                            selectedInfoMeta.value = infoMetaDataByKey["intakeDatesMetaData"]
                        },
                    )
                    details.intakeDates.forEach { (label, value) ->
                        KeyValueRow(label = label, value = value)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                PrimaryButton(
                    text = "Apply Now",
                    onClick = { onAction(CourseDetailsAction.ApplyNow) },
                    modifier = Modifier.padding(bottom = 24.dp),
                )
            }
        }
        InfoMetaDialog(
            meta = selectedInfoMeta.value,
            onDismiss = { selectedInfoMeta.value = null },
        )
    }
}

@Composable
private fun InfoMetaDialog(
    meta: InfoMetaData?,
    onDismiss: () -> Unit,
) {
    if (meta == null) return
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                androidx.compose.material3.Text("Close")
            }
        },
        title = {
            Text(
                text = meta.title,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column {
                meta.information.forEachIndexed { index, block ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    val subtitle = block.subtitle
                    val hasSubtitle = !subtitle.isNullOrBlank()
                    val descriptions = block.description

                    when {
                        hasSubtitle -> {
                            InfoBulletLine(
                                text = subtitle,
                                fontWeight = FontWeight.SemiBold,
                            )
                            if (descriptions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                descriptions.forEach { line ->
                                    InfoBulletLine(
                                        text = line,
                                        modifier = Modifier.padding(start = 12.dp),
                                    )
                                }
                            }
                        }
                        else -> {
                            descriptions.forEach { line ->
                                InfoBulletLine(text = line)
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun InfoBulletLine(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = "• ",
            fontWeight = fontWeight,
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontWeight = fontWeight,
        )
    }
}

@Composable
private fun BannerImage(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = Color.Gray),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Course hero image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun AboutUsSection(aboutUs: String) {
    SectionHeader(title = "About Us")
    Spacer(modifier = Modifier.height(8.dp))
    ReadMoreText(text = aboutUs, readMoreLabel = "Read More")
}

@Composable
private fun CampusLifeSection(
    items: List<CampusLifeItem>,
    onItemClick: (CampusLifeItem) -> Unit,
) {
    SectionHeader(title = "Campus Life")
    Spacer(modifier = Modifier.height(12.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 0.dp),
    ) {
        items(
            items = items,
            key = { it.title },
        ) { item ->
            val hasUrl = !item.videoUrl.isNullOrBlank()
            CampusLifeCard(
                item = item,
                modifier = Modifier.width(200.dp),
                onClick = if (hasUrl) {
                    { onItemClick(item) }
                } else null,
            )
        }
    }
}

@Composable
private fun LocationSection(
    latitude: Double?,
    longitude: Double?,
    onLocationClick: (latitude: Double, longitude: Double) -> Unit,
) {
    SectionHeader(title = "Location")
    Spacer(modifier = Modifier.height(12.dp))
    val mapImageUrl = remember(latitude, longitude) {
        if (latitude != null && longitude != null) {
            googleStaticMapImageUrl(latitude, longitude, BuildKonfig.GOOGLE_MAPS_API_KEY)
        } else {
            null
        }
    }
    LocationMapCard(
        mapImageUrl = mapImageUrl,
        onClick = if (latitude != null && longitude != null) {
            { onLocationClick(latitude, longitude) }
        } else null,
    )
}

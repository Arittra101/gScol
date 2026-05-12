package org.getscol.gscol.feature.course_details.presentation.course_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.getscol.gscol.BuildKonfig
import org.getscol.gscol.core.helper.iso4217CurrencySymbol
import org.getscol.gscol.core.helper.toNavJson
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.HorizontalPaddingBleed
import org.getscol.gscol.core.presentation.components.KeyValueRow
import org.getscol.gscol.core.presentation.components.PrimaryButton
import org.getscol.gscol.core.presentation.components.ReadMoreText
import org.getscol.gscol.core.presentation.components.ScrollableTabs
import org.getscol.gscol.core.presentation.components.SectionHeader
import org.getscol.gscol.feature.course_details.domain.model.AboutUs
import org.getscol.gscol.feature.course_details.domain.model.CampusLifeItem
import org.getscol.gscol.feature.course_details.domain.model.CourseLocation
import org.getscol.gscol.feature.course_details.domain.model.FeeItems
import org.getscol.gscol.feature.course_details.domain.model.InfoMetaData
import org.getscol.gscol.feature.course_details.presentation.components.CampusLifeCard
import org.getscol.gscol.feature.course_details.presentation.components.CourseOverviewCard
import org.getscol.gscol.feature.course_details.presentation.components.LocationMapCard
import org.getscol.gscol.feature.course_details.presentation.components.googleMapsOpenUrl
import org.getscol.gscol.feature.course_details.presentation.components.googleStaticMapImageUrl
import org.getscol.gscol.feature.course_details.presentation.components.normalizeCoordinatesLink
import org.getscol.gscol.feature.course_details.presentation.components.parseLatLngFromMapsUrl
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

private val DEFAULT_TAB_TITLES = listOf(
    "About us",
    "Campus Life",
    "Location",
    "Academic Requirements",
    "Fees & Scholarships",
    "Intake Dates",
)

@Composable
fun CourseDetailsScreenRoot(
    courseId: String,
    navigator: Navigator,
    viewModel: CourseDetailsViewModel = koinViewModel(
        key = "CourseDetails-$courseId",
        parameters = { parametersOf(courseId) }
    ),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CourseDetailsUiEffect.NavigateBack -> navigator.navigateBack()
                is CourseDetailsUiEffect.ApplyNow ->  {
                    navigator.navigateTo(Route.ApplicationFormRoute(courseDetails = state.courseDetails.toNavJson()))
                }
            }
        }
    }

    val uriHandler = LocalUriHandler.current
    CourseDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = navigator::navigateBack,
        onCampusLifeItemClick = { item ->
            when {
                !item.videoUrl.isNullOrBlank() ->
                    navigator.navigateTo(
                        Route.CourseVideoPlayer(
                            title = item.title,
                            videoUrl = item.videoUrl
                        )
                    )
            }
        },
        onOpenMapsUrl = { url -> uriHandler.openUri(url) },
    )
}

@Composable
fun CourseDetailsScreen(
    state: CourseDetailsState,
    onAction: (CourseDetailsAction) -> Unit,
    onBack: () -> Unit,
    onCampusLifeItemClick: (CampusLifeItem) -> Unit = {},
    onOpenMapsUrl: (String) -> Unit = {},
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
        val metaByKey = remember(details.meta) { details.meta.associateBy { it.infoKey } }
        val tabTitles = remember(details.tabs) {
            details.tabs.map { it.label }.ifEmpty { DEFAULT_TAB_TITLES }
        }
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
                .background(colors.customSurface),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                BannerImage(imageUrl = details.university.uniCoverImageUrl.orEmpty())
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .offset(y = (-80).dp),
                ) {
                CourseOverviewCard(
                    courseName = details.courseName,
                    ranking = details.ranking,
                    universityName = details.university.uniName,
                    universityLogoUrl = details.university.uniLogoUrl.orEmpty(),
                    tags = details.tags,
                    onRankingInfoClick = if (details.ranking?.hasInfo == true) {
                        {
                            details.ranking.infoKey?.let { selectedInfoMeta.value = metaByKey[it] }
                        }
                    } else null,
                )
                Spacer(modifier = Modifier.height(16.dp))
                ScrollableTabs(
                    tabs = tabTitles,
                    selectedIndex = state.selectedTabIndex,
                    onTabSelected = {
                        tabsEnabled.value = true
                        onAction(CourseDetailsAction.TabSelected(it))
                    },
                    horizontalOutdent = 20.dp,
                    scrollLeadingPadding = 20.dp,
                    scrollTrailingPadding = 20.dp,
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
                        items = details.campusLife?.videoUrls.orEmpty().mapIndexed { index, url ->
                            CampusLifeItem(
                                title = "Video ${index + 1}",
                                thumbnailUrl = null,
                                duration = null,
                                count = null,
                                isVideo = true,
                                videoUrl = url,
                            )
                        },
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
                        location = details.location,
                        onOpenMapsUrl = onOpenMapsUrl,
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
                        showInfoIcon = details.academicRequirements?.infoKey
                            ?.let { metaByKey[it]?.information?.isNotEmpty() == true } == true,
                        onInfoClick = {
                            details.academicRequirements?.infoKey?.let {
                                selectedInfoMeta.value = metaByKey[it]
                            }
                        },
                    )
                    details.academicRequirements?.requirements?.degreeRequirements.orEmpty()
                        .forEach { req ->
                            KeyValueRow(
                                label = req.degreeName.orEmpty().plus(" (" + req.label + ")"),
                                value = req.minValue.orEmpty(),
                            )
                        }
                    details.academicRequirements?.requirements?.englishRequirements.orEmpty()
                        .forEach { req ->
                            KeyValueRow(
                                label = req.testName.orEmpty(),
                                value = listOfNotNull(
                                    req.minOverallValue,
                                    req.minSectionValue,
                                ).joinToString(" / "),
                            )
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
                        showInfoIcon = details.feesAndScholarships?.infoKey
                            ?.let { metaByKey[it]?.information?.isNotEmpty() == true } == true,
                        onInfoClick = {
                            details.feesAndScholarships?.infoKey?.let {
                                selectedInfoMeta.value = metaByKey[it]
                            }
                        },
                    )
                    details.feesAndScholarships?.items?.tuitionFees?.let { tf ->
                        val amount = tf.amount.orEmpty()
                        val currencyCode = tf.currency.orEmpty()
                        val currencyLabel =
                            currencyCode.ifBlank { null }?.let { iso4217CurrencySymbol(it) }
                        val frequency = tf.frequency.orEmpty()
                        KeyValueRow(
                            label = "Tuition Fees",
                            value = listOfNotNull(
                                listOfNotNull(
                                    currencyLabel,
                                    amount.ifBlank { null }).joinToString(" ").ifBlank { null },
                                frequency.ifBlank { null },
                            ).joinToString(" / "),
                        )
                    }
                    details.feesAndScholarships?.items?.initialDeposit?.let { dep ->
                        KeyValueRow(label = "Initial Deposit", value = dep)
                    }
                    details.feesAndScholarships?.items?.applicationFee?.let { fee ->
                        KeyValueRow(label = "Application Fee", value = fee)
                    }
                    if (details.feesAndScholarships?.items.hasScholarshipInfo()) {
                        KeyValueRow(
                            label = "Scholarships",
                            value = "Available",
                            valueColor = colors.customPrimary,
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
                        showInfoIcon = details.intakeDates?.infoKey
                            ?.let { metaByKey[it]?.information?.isNotEmpty() == true } == true,
                        onInfoClick = {
                            details.intakeDates?.infoKey?.let {
                                selectedInfoMeta.value = metaByKey[it]
                            }
                        },
                    )
                    KeyValueRow(
                        "Intakes",
                        details.intakeDates?.intakes.orEmpty().joinToString(", ")
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                }
            }
            PrimaryButton(
                text = "Apply Now",
                onClick = { onAction(CourseDetailsAction.ApplyNow) },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
            )
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
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
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
private fun AboutUsSection(aboutUs: AboutUs?) {
    SectionHeader(title = "About Us")
    Spacer(modifier = Modifier.height(8.dp))
    val text = aboutUs?.description?.joinToString("\n\n").orEmpty()
    ReadMoreText(text = text, readMoreLabel = "Read More")
}

@Composable
private fun CampusLifeSection(
    items: List<CampusLifeItem>,
    onItemClick: (CampusLifeItem) -> Unit,
) {
    SectionHeader(title = "Campus Life")
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalPaddingBleed(outdent = 20.dp) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
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
}

@Composable
private fun LocationSection(
    location: CourseLocation?,
    onOpenMapsUrl: (String) -> Unit,
) {
    SectionHeader(title = "Location")
    Spacer(modifier = Modifier.height(12.dp))
    val coords = location?.coordinates
    val link =
        coords?.link?.trim()?.takeIf { it.isNotEmpty() }?.let { normalizeCoordinatesLink(it) }
    val parsedFromLink = remember(link) { link?.let { parseLatLngFromMapsUrl(it) } }
    val latitude = coords?.latitude ?: parsedFromLink?.first
    val longitude = coords?.longitude ?: parsedFromLink?.second
    val openUrl = link
        ?: if (latitude != null && longitude != null) googleMapsOpenUrl(
            latitude,
            longitude
        ) else null
    val mapImageUrl = remember(latitude, longitude) {
        if (latitude != null && longitude != null) {
            googleStaticMapImageUrl(latitude, longitude, BuildKonfig.GOOGLE_MAPS_API_KEY)
        } else {
            null
        }
    }
    LocationMapCard(
        mapImageUrl = mapImageUrl,
        onClick = openUrl?.let { url -> { onOpenMapsUrl(url) } },
    )
}

private fun FeeItems?.hasScholarshipInfo(): Boolean {
    if (this == null) return false
    val s = scholarshipDetails
    val structured = s != null && sequenceOf(
        s.scholarshipName,
        s.scholarshipAmount,
        s.currency,
        s.scholarshipType,
    ).any { !it.isNullOrBlank() }
    val textFromApi = !scholarshipsText.isNullOrBlank()
    return structured || textFromApi
}

package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.jetbrains.compose.resources.painterResource
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.route
import scol.composeapp.generated.resources.scol_text_logo_2

@Composable
fun HomeAppBar(navigator: Navigator, action: (HomeAction) -> Unit) {
    val colors = appColors()
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(color = colors.customSecondaryContainer)
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.scol_text_logo_2),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.width(50.dp)
                        .padding(bottom = 2.dp).clickable {
                            /*   action(HomeAction.Change)*/
                        },
                )
                HomeSearchBar(
                    modifier = Modifier.weight(1f),
                    onClick = { navigator.navigateToRoute(Route.Search) }
                )
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = colors.customPrimary,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(Res.drawable.route),
                            contentDescription = "Trace"
                        )
                    }
                }

                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = colors.customPrimary,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = "Notifications"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            EligibilityButton(navigator)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = appColors()
    val rowModifier = modifier
        .height(36.dp)
        .padding(start = 12.dp)
        .background(
            color = colors.customPrimaryContainer,
            shape = RoundedCornerShape(50.dp)
        )
        .border(
            width = 1.dp,
            color = colors.customInfo,
            shape = RoundedCornerShape(50.dp)
        )
        .padding(horizontal = 12.dp)
        .then(
            Modifier.clickable(onClick = onClick)
        )

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = colors.customInfo,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))


        Text(
            "Search country, course, intake",
            fontSize = 11.sp,
            color = colors.customInfo,
            modifier = Modifier.weight(1f)
        )
    }
}

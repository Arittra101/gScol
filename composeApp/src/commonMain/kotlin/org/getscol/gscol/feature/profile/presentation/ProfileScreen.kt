package org.getscol.gscol.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.profile

// Color constants
private val PrimaryRed = Color(0xFFB71C1C)
private val LightPink = Color(0xFFFCE4EC)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF9E9E9E)
private val DividerColor = Color(0xFFF0F0F0)
private val BackgroundColor = Color(0xFFFAF7F4)

@Composable
fun ProfileScreenRoute(
    navigator: Navigator? = null,
    viewmodel: ProfileViewmodel = koinViewModel(),
) {
    BaseScreen(
        title = stringResource(Res.string.profile),
        showBackButton = false,
        isTopLevelScreen = true
    ) {
        ProfileScreen(navigator = navigator, viewmodel = viewmodel)
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigator: Navigator? = null,
    viewmodel: ProfileViewmodel = koinViewModel()
) {
    val onAction = viewmodel::onAction
    val isUserLogin by viewmodel.session.isUserLoggedIn.collectAsState(false)
    val fullName by viewmodel.session.userFullName.collectAsState(initial = null)
    val joinedAt by viewmodel.session.userJoinedAt.collectAsState(initial = null)

    // Bottom sheet visibility + state
    var showLogoutSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Actual logout — runs after user confirms in the sheet
    val performLogout: () -> Unit = {
        onAction(ProfileAction.OnResetPref)
        navigator?.navigateTo(Route.HomeRoute, true)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Main profile content ────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .then(if (!isUserLogin) Modifier.blur(20.dp) else Modifier)
        ) {
            ProfileHeader(
                userName = fullName ?: "—",
                joinYear = joinedAt?.toString() ?: "—",
                onEditInformation = { navigator?.navigateTo(Route.EditProfile) }
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            ProfileMenuItem(
                icon = Icons.Default.Tune,
                title = "Preferences",
                subtitle = "Manage your app experience",
                onClick = {}
            )

            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            ProfileMenuItem(
                icon = Icons.Default.Headset,
                title = "Support Center",
                subtitle = "Get help & FAQ",
                onClick = {}
            )

            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            // ← opens confirmation sheet instead of logging out immediately
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Log Out",
                subtitle = "End your session securely",
                onClick = { showLogoutSheet = true },
                isDestructive = true
            )

            HorizontalDivider(color = DividerColor, thickness = 1.dp)
        }
    }

    // ── Logout confirmation bottom sheet ───────────────────────────────
    if (showLogoutSheet) {
        LogoutConfirmationSheet(
            sheetState = sheetState,
            onConfirm = {
                // Hide sheet first, then navigate
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showLogoutSheet = false
                    performLogout()
                }
            },
            onDismiss = {
                // Just close the sheet — no navigation
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showLogoutSheet = false
                }
            }
        )
    }
}

// ── Logout confirmation bottom sheet ────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogoutConfirmationSheet(
    sheetState: SheetState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Icon badge
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(LightPink, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = PrimaryRed,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Log Out",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Are you sure you want to log out of your account?",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Confirm → logout + navigate ─────────────────────────────
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryRed,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Yes, Log Out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Cancel → dismiss only ───────────────────────────────────
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ── Profile sub-composables ──────────────────────────────────────────────────
@Composable
private fun ProfileHeader(
    userName: String,
    joinYear: String,
    onEditInformation: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(BackgroundColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFD7B9A0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Joined $joinYear",
                fontSize = 14.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Edit Information",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryRed,
                modifier = Modifier.clickable { onEditInformation() }
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(appColors().customPrimary.copy(alpha = 0.10f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryRed,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LogoutSheetPreview() {
    MaterialTheme {
        LogoutConfirmationSheet(
            sheetState = rememberModalBottomSheetState(),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoggedInPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                ProfileHeader("Sophia Carter", "2023") {}
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = DividerColor)
                ProfileMenuItem(Icons.Default.Tune, "Preferences", "Manage your app experience", {})
                HorizontalDivider(color = DividerColor)
                ProfileMenuItem(Icons.Default.Headset, "Support Center", "Get help & FAQ", {})
                HorizontalDivider(color = DividerColor)
                ProfileMenuItem(Icons.AutoMirrored.Filled.ExitToApp, "Log Out", "End your session securely", {}, true)
                HorizontalDivider(color = DividerColor)
            }
        }
    }
}
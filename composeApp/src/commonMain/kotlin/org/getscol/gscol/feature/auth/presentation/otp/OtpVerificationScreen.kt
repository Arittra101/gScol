package org.getscol.gscol.feature.auth.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpVerificationScreenRoot(
    viewModel: OtpVerificationViewModel = koinViewModel(),
    navigator: Navigator,
    otpNumber: String? = null
) {
    val state by viewModel.state.collectAsState()

    // Handle successful verification
    if (state.isVerificationSuccessful) {
        navigator.navigateToOtherScreen(route = Route.Desire)
    }
    state.otp = otpNumber.orEmpty()

    OtpVerificationScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator,
        otpNumber = otpNumber
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    state: OtpVerificationState,
    onAction: (OtpVerificationAction) -> Unit,
    navigator: Navigator,
    otpNumber: String? = null
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Verification",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateAuthScreenBack(Route.OtpVerification()) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
            /*.padding(24.dp)*/,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(116.dp))

            // Title
            Text(
                text = "Enter Verification Code",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Subtitle
            Text(
                text = "We sent a 6-digit code to your phone \nnumber.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(28.dp))

            // OTP Input Boxes
            OtpInputField(
                otpLength = 6,
                value = state.otp,
                onValueChange = { newValue ->
                    if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                        onAction(OtpVerificationAction.OnOtpChange(newValue))
                    }
                },
                enabled = !state.isTokenExpired && !state.isVerifying
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (state.otpError != null) {
                Text(
                    text = state.otpError,
                    fontSize = 12.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    fontSize = 12.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            val minutes = state.tokenExpirationSeconds / 60
            val seconds = state.tokenExpirationSeconds % 60
            val timeText =
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF525252))
                    ) {
                        append("Code expires in: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = if (state.tokenExpirationSeconds < 30) Color.Red else Color.DarkGray
                        )
                    ) {
                        append(timeText)
                    }
                },
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onAction(OtpVerificationAction.OnVerifyClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B3838),
                    disabledContainerColor = Color(0xFF8B3838).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isVerifying && !state.isTokenExpired
            ) {
                if (state.isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Verify",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if(state.isTokenExpired) {
                Text(
                    text = "Didn't receive the code?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.clickable{
                        println("click")
                        onAction(OtpVerificationAction.OnResendClick) }
                )
            }

            if (state.resendingOtp) {
                val resendMinutes = state.resendAvailableSeconds / 60
                val resendSeconds = state.resendAvailableSeconds % 60
                Text(
                    text = "Resend code in ${
                        resendMinutes.toString().padStart(2, '0')
                    }:${resendSeconds.toString().padStart(2, '0')}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )

            }
        }
    }
}

@Composable
fun OtpInputField(
    otpLength: Int,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(otpLength) { index ->
                    val char = value.getOrNull(index)?.toString() ?: ""
                    val isFocused = value.length == index

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                color = if (enabled) Color(0xFFF5F5F5) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isFocused) 2.dp else 0.dp,
                                color = if (isFocused) Color(0xFF8B3838) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (enabled) Color.Black else Color.Gray
                            )
                        )
                    }
                }
            }
        }
    )
}

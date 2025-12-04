package org.getscol.gscol.auth.presentation.verfication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.auth.presentation.AuthViewmodel
import org.getscol.gscol.navigation.NavigationAction
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpVerificationScreenRoot(navigator: Navigator,viewmodel: AuthViewmodel = koinViewModel()) {
    OtpVerificationScreen(navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(navigator: Navigator) {
    var code by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(59) }

    // Timer effect
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            kotlinx.coroutines.delay(1000)
            timeLeft--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Verification",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Title
            Text(
                text = "Enter Verification Code",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "We sent a 6-digit code to your email address.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // OTP Input Boxes
            OtpInputField(
                otpLength = 6,
                value = code,
                onValueChange = { newValue ->
                    if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                        code = newValue
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Timer
            val minutes = timeLeft / 60
            val seconds = timeLeft % 60
            Text(
                text = "Code expires in:   ${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Didn't receive code
            Text(
                text = "Didn't receive the code?",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.weight(1f))

            // Verify Button
            Button(
                onClick = { navigator.navigateTo(NavigationAction.SuccessFullLogInNavigation(Route.HomeRoute,Route.Login)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B3838)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Verify",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun OtpInputField(
    otpLength: Int,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(otpLength) { index ->
                    val char = value.getOrNull(index)?.toString() ?: ""
                    val isFocused = value.length == index

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = Color(0xFFF5F5F5),
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
                                color = Color.Black
                            )
                        )
                    }
                }
            }
        }
    )
}

package org.getscol.gscol.feature.auth.presentation.resetpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.core.components.AppTextField
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.enter_confirm_pass
import scol.composeapp.generated.resources.enter_pass
import scol.composeapp.generated.resources.enter_phone_text

@Composable
fun ResetPasswordRoute(
    viewModel: ResetPasswordViewmodel = koinViewModel(),
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onAction

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ResetPasswordUiEffect.PasswordResetSuccess -> {
                    println("ResetPasswordRoute again")
                    val devOtp = viewModel.state.value.devOtp
                    navigator.navigateToAuthScreen(Route.OtpVerification(devOtp))
                }

                is ResetPasswordUiEffect.ShowToast -> {
                    /* Toast.makeText(context, effect.message, Toast.LENGTH_SHORT)
                         .show()*/
                }

                ResetPasswordUiEffect.NavigateBack -> {
//                    navController.popBackStack()
                }
            }
        }
    }


    ResetPasswordScreen(state = state, navigator, onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    state: ResetPasswordUiState,
    navigator: Navigator,
    onAction: (ResetPasswordAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reset Password",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateAuthScreenBack(Route.SignUp) }) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 140.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Reset Password",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Your identity has been verified. Set\n" + "your new password.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(44.dp))

                // Phone number field
                AppTextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onAction(ResetPasswordAction.OnPhoneNumberChange(it))
                    },
                    label = stringResource(Res.string.enter_phone_text),
                    keyboardType = KeyboardType.Phone,
                    errorText = state.phoneError,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // password field
                AppTextField(
                    value = state.password,
                    onValueChange = { onAction(ResetPasswordAction.OnPasswordChange(it)) },
                    label = stringResource(Res.string.enter_pass),
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    errorText = state.passwordError,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // confirm password field
                AppTextField(
                    value = state.confirmPassword,
                    onValueChange = { onAction(ResetPasswordAction.OnConfirmPasswordChange(it)) },
                    label = stringResource(Res.string.enter_confirm_pass),
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    errorText = state.confirmPasswordError,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(
                    modifier =
                        if (state.errorMessage != null) Modifier.height(20.dp)
                        else Modifier.height(0.dp)
                )

                // Error message
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Send OTP button
                Button(
                    onClick = { onAction(ResetPasswordAction.OnClickSubmit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B3838),
                        disabledContainerColor = Color(0xFF8B3838).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Submit",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
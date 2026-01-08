package org.getscol.gscol.auth.presentation.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.auth.presentation.registration.components.RegistrationUiEffect
import org.getscol.gscol.auth.presentation.registration.components.TermsAndPrivacyCheckBox
import org.getscol.gscol.core.components.AppTextField
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.already_have_account
import scol.composeapp.generated.resources.confirmed_password_text
import scol.composeapp.generated.resources.create_account
import scol.composeapp.generated.resources.create_new_account
import scol.composeapp.generated.resources.enter_name_text
import scol.composeapp.generated.resources.enter_pass
import scol.composeapp.generated.resources.enter_phone_text
import scol.composeapp.generated.resources.login_text
import scol.composeapp.generated.resources.register_today
import scol.composeapp.generated.resources.signup_text

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreenRoot(
    viewModel: RegistrationViewModel = koinViewModel(),
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffectState.collect { effect -> when (effect) {
                is RegistrationUiEffect.RegistrationSuccess -> {
                    navigator.navigateToAuthScreen(Route.OtpVerification)
                }
                is RegistrationUiEffect.ShowToast -> {
//                     Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is RegistrationUiEffect.NavigateBack -> {
//                    navController.popBackStack()
                }
            }
        }
    }

    BackHandler { navigator.navigateAuthScreenBack(Route.SignUp) }
    RegistrationScreen(state = state, onAction = viewModel::onAction,navigator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
    navigator: Navigator
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.create_account),
                        modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.navigateAuthScreenBack(Route.SignUp) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    stringResource(Res.string.create_new_account),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    stringResource(Res.string.register_today),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Full name field
                AppTextField(
                    value = state.fullName,
                    onValueChange = { onAction(RegistrationAction.OnFullNameChange(it)) },
                    label = stringResource(Res.string.enter_name_text),
                    errorText = state.fullNameError,
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Phone number field
                AppTextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onAction(RegistrationAction.OnPhoneNumberChange(it))
                    },
                    label = stringResource(Res.string.enter_phone_text),
                    keyboardType = KeyboardType.Phone,
                    errorText = state.phoneError,
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Password field
                AppTextField(
                    value = state.password,
                    onValueChange = {
                        onAction(RegistrationAction.OnPasswordChange(it))
                    },
                    label = stringResource(Res.string.enter_pass),
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    errorText = state.passwordError,
                )
                Spacer(modifier = Modifier.height(10.dp))

                //Confirm password field
                AppTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        onAction(RegistrationAction.OnConfirmPassWordChange(it))
                    },
                    label = stringResource(Res.string.confirmed_password_text),
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    errorText = state.confirmPasswordError,
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Error message
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Register button
                Button(
                    onClick = { onAction(RegistrationAction.OnRegisterClick) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B3838),
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            stringResource(Res.string.signup_text),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Navigate to login
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(Res.string.already_have_account),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        stringResource(Res.string.login_text),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { navigator.navigateAuthScreenBack(Route.SignUp) }
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                TermsAndPrivacyCheckBox(
                    isTermsAccepted = state.isTermsAccepted,
                    onCheckedChange = {
                        onAction(RegistrationAction.OnTermsAcceptedChange(it))
                    },
                    onTermsClick = {
                        // navigate to Terms screen
                    },
                    onPrivacyClick = {
                        // navigate to Privacy screen
                    }
                )
            }
        }
    }
}

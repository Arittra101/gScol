package org.getscol.gscol.feature.auth.presentation.login

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.core.components.AppTextField
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.dont_have_account
import scol.composeapp.generated.resources.enter_pass
import scol.composeapp.generated.resources.enter_phone_text
import scol.composeapp.generated.resources.forget_password
import scol.composeapp.generated.resources.login_text
import scol.composeapp.generated.resources.scol_hat_logo
import scol.composeapp.generated.resources.signup_text
import scol.composeapp.generated.resources.skip_text
import scol.composeapp.generated.resources.welcome_text

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginUiEffect.LoginSuccess -> {
                    if (effect.isUserFillupAcademicForm > 0) {
                        navigator.navigateTo(Route.HomeRoute,true)
                    } else {
                        navigator.navigateTo(Route.AcademicForm, true)
                    }
                }
                is LoginUiEffect.ShowToast -> { }
            }
        }
    }

    LoginScreen(state = state, onAction = viewModel::onAction, navigator = navigator)
    BackHandler { navigator.navigateAuthScreenBack(Route.Login) }
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    navigator: Navigator
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(130.dp))
            Image(
                painter = painterResource(resource = Res.drawable.scol_hat_logo),
                contentDescription = "Logo",
                modifier = Modifier.height(70.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                stringResource(Res.string.welcome_text),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(56.dp))

            // Phone number field
            AppTextField(
                value = state.phoneNumber,
                onValueChange = {
                    onAction(LoginAction.OnPhoneNumberChange(it))
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
                    onAction(LoginAction.OnPasswordChange(it))
                },
                label = stringResource(Res.string.enter_pass),
                isPassword = true,
                keyboardType = KeyboardType.Password,
                errorText = state.passwordError,
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Forgot Password link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    stringResource(Res.string.forget_password),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.clickable { navigator.navigateToAuthScreen(Route.ResetPassword) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Login button
            Button(
                onClick = { onAction(LoginAction.OnLoginClick) },
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
                        stringResource(Res.string.login_text),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigate to registration
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(Res.string.dont_have_account),
                    modifier = Modifier.padding(end = 2.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    stringResource(Res.string.signup_text),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable { navigator.navigateToAuthScreen(Route.SignUp) }
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable(onClick = { navigator.navigateAuthScreenBack(Route.Login) })
            ) {
                Text(
                    stringResource(Res.string.skip_text),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                )
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = "Arrow Right",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

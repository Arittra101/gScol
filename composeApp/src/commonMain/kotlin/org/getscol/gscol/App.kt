package org.getscol.gscol

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.getscol.gscol.auth.data.GoogleAuthProvider
import org.getscol.gscol.core.data.auth.AuthTokenProvider
import org.getscol.gscol.theme.ScolTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    ScolTheme {
        println("App1 createyy")
        var showContent by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val provider: GoogleAuthProvider = getKoin().get()

        //for testing purposes
        val authTokenProvider: AuthTokenProvider = getKoin().get()
        LaunchedEffect(Unit) {
            val a = authTokenProvider.getAccessToken()
            println("access token get App  $a")
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    showContent = !showContent

                    println("Result here=")

                    scope.launch {
                        val result = provider.getGoogleAuthToken()
                        println("Result = $result")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Click me App with ios!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
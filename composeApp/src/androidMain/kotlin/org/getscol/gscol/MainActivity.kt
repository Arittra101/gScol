package org.getscol.gscol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import org.getscol.gscol.theme.ScolTheme
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.getscol.gscol.navigation.TopLevelDestination
import org.getscol.gscol.navigation.rememberNavigator
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.getscol.gscol.theme.ScolTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ScolTheme {
                ScolApp()
            }
        }
    }
}




//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}
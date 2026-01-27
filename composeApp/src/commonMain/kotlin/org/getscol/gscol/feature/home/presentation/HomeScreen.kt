package org.getscol.gscol.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.getscol.gscol.feature.home.presentation.components.CourseInfoCard
import org.getscol.gscol.feature.home.presentation.components.HomeAppBar
import org.getscol.gscol.navigation.Navigator

@Composable
fun HomeScreenRoot(
    navigator: Navigator
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            HomeAppBar()
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFCCCCCC)),

            ) {
            LazyColumn(
                modifier = Modifier
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(bottom = 52.dp),
            ) {
                items(10) {
                    CourseInfoCard(
                        courseName = "MSc International Business Management",
                        city = "Westdown",
                        country = "UK",
                        universityName = "University of Winchester",
                        universityLogo = "https://as2.ftcdn.net/v2/jpg/02/82/57/53/1000_F_282575364_W1Mz7QqrvkdLN18XcIO3vomBBc0B8FYV.jpg",
                        backgroundImage = "https://images.unsplash.com/photo-1562774053-701939374585",
                        intake = "JAN 2026",
                        tuitionFees = "$58,000",
                        duration = "1 year",
                        scholarship = "Upto $5000",
                        initialDeposit = "$20,000",
                        ieltsBand = "6.5",
                        ieltsSingleBand = "5.5",
                        isFavorite = false,
                        onFavoriteClick = {},
                        onApplyClick = {}
                    )
                }
            }
        }
    }
}
package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.eligibility_icon

@Composable
@Preview
fun EligibilityButton(navigator: Navigator) {
    Button(
        onClick = {
            navigator.navigateToAuthScreen(Route.Login)
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors().customPrimary.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.eligibility_icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Check Your Eligibility",
            color = appColors().customPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
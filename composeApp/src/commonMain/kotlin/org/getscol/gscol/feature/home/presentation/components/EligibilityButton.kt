package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
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

@Composable
fun EligibilityButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFE4E1)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            Icons.Default.Checklist,
            contentDescription = null,
            tint = Color(0xFF8B0000)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Check Your Eligibility",
            color = Color(0xFF8B0000),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
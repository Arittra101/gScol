package org.getscol.gscol.feature.application.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.application.domain.model.response.Stage

@Composable
fun StatusBadge(
    status: Stage?,
    modifier: Modifier = Modifier
) {
    Text(
        text = status?.applicationStageName.orEmpty(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
            .background(color = Color(0xFF8B3838), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    )
}

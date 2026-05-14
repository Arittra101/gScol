package org.getscol.gscol.feature.application.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorNextStepsBg
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorTextPrimary
import org.getscol.gscol.feature.application.presentation.application_status_tracker.ColorTextSub


@Composable
fun NextStepsCard(text: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorNextStepsBg)
            .padding(16.dp)
    ) {
        Text(
            text = "Next Steps",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ColorTextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = buildAnnotatedString {
                append("Please prepare for your upcoming ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF8B3838))) {
                    append(text.orEmpty() + " stage")
                }
                append(". Ensure that you have gathered all the necessary documentation. We wish you the best of luck!")
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = ColorTextSub,
            lineHeight = 20.sp
        )
    }
}

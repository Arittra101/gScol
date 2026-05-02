package org.getscol.gscol.feature.application.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val PrimaryRed   = Color(0xFFB5292A)
private val FieldBorder  = Color(0xFFDDDDDD)
private val FieldBg      = Color(0xFFF9F9F9)
private val LabelColor   = Color(0xFF666666)
private val InputText    = Color(0xFF0D171B)

@Composable
fun FormField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = LabelColor,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryRed,
                unfocusedBorderColor = FieldBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = FieldBg,
                focusedTextColor = InputText,
                unfocusedTextColor = InputText,
                cursorColor = PrimaryRed
            ),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
    }
}
 
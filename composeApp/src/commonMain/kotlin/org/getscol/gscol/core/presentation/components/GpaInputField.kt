package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GpaInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Regex: Allows digits, and optionally one '.' followed by more digits
                // Matches: "12", "12.", "12.5"
                if ((newValue.isEmpty() || newValue.matches(Regex("""^\d+\.?\d*$""")) && (keyboardType == KeyboardType.Decimal))) {
                    onValueChange(newValue)
                } else if (keyboardType == KeyboardType.Text) {
                    onValueChange(newValue)
                }
            },
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD9E8ED),
                focusedContainerColor = Color(0xFFD9E8ED),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 15.sp
            )
        )
    }
}
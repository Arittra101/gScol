package org.getscol.gscol.feature.application.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val PrimaryRed   = Color(0xFFB5292A)
private val FieldBorder  = Color(0xFFDDDDDD)
private val FieldBg      = Color(0xFFF9F9F9)
private val LabelColor   = Color(0xFF666666)
private val InputText    = Color(0xFF0D171B)
private val HintText     = Color(0xFFAAAAAA)


@Composable
fun IntakeDropdown(
    label: String,
    selected: String?,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
 
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = LabelColor,
            modifier = Modifier.padding(bottom = 6.dp)
        )
 
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FieldBg)
                    .border(
                        width = 1.dp,
                        color = if (expanded) PrimaryRed else FieldBorder,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selected ?: "",
                    fontSize = 14.sp,
                    color = if (selected == null) HintText else InputText
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = LabelColor,
                    modifier = Modifier.size(20.dp)
                )
            }
 
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                color = InputText
                            )
                        },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
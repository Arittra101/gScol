package org.getscol.gscol.feature.consultant.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.consultant.domain.model.ConsultantModel
import org.getscol.gscol.theme.appColors
import org.jetbrains.compose.resources.painterResource
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.fardeen_ishaque
import scol.composeapp.generated.resources.raihan_ul_islam
import scol.composeapp.generated.resources.shafayat_jamil

@Composable
fun ConsultantCard(
    consultant: ConsultantModel,
    avatarColor: Color,
    onViewProfile: () -> Unit,
    onBookSession: () -> Unit
) {
    /// get image resource
    val imageResource = when (consultant.image) {
        "raihan_ul_islam" -> painterResource(Res.drawable.raihan_ul_islam)
        "fardeen_ishaque" -> painterResource(Res.drawable.fardeen_ishaque)
        "shafayat_jamil" -> painterResource(Res.drawable.shafayat_jamil)
        else -> painterResource(Res.drawable.raihan_ul_islam)
    }
    val colors = appColors()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                Image(
                    painter = imageResource,
                    contentDescription = "Arrow",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = consultant.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.customPrimaryText
                    )
                    Text(text = consultant.title, fontSize = 13.sp, color = colors.customInfo)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Email,
                            null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = consultant.email, fontSize = 12.sp, color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Phone,
                            null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = consultant.phone, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    null,
                    tint = Color.Gray,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = consultant.officeHours, fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onViewProfile,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColors().customPrimary.copy(
                            alpha = 0.1f
                        )
                    )
                ) {
                    Text("View Profile", fontSize = 14.sp, color = appColors().customPrimary)
                }
                Button(
                    onClick = onBookSession,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = appColors().customPrimary)
                ) {
                    Text("Book", fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}
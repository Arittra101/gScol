package org.getscol.gscol.feature.application.presentation.application_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.getscol.gscol.core.presentation.components.rememberPdfPicker
import org.getscol.gscol.feature.application.domain.model.response.DocumentCheckList
import org.getscol.gscol.feature.application.domain.model.response.UploadedDocument
import org.getscol.gscol.feature.application.presentation.DocumentCategoryState
import org.getscol.gscol.feature.application.presentation.application_details.ApplicationDetailAction
import org.getscol.gscol.feature.application.presentation.application_details.DocumentTheme
import scol.composeapp.generated.resources.Res

@Composable
fun DocumentsSection(
    documentCheckList: List<DocumentCheckList>,
    action: (ApplicationDetailAction) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Documents",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DocumentTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Note: we use Column instead of LazyColumn
        // because this is already inside a verticalScroll
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            documentCheckList.forEach { category ->
                key(category.documentTypeId) {
                    val isExpanded = category.isExpandable
                    DocumentCategoryCard(
                        category = category,
                        isExpanded = isExpanded,
                        action = action
                    )
                }
            }
        }
    }
}



@Composable
fun DocumentCategoryCard(
    category: DocumentCheckList,
    isExpanded: Boolean,
    action: (ApplicationDetailAction) -> Unit,
) {
    val chevronAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300, easing = EaseInOutCubic),
        label = "chevron_rotation"
    )
    val shape = RoundedCornerShape(14.dp)

    val pickPdf = rememberPdfPicker { pickedFile ->
        action(ApplicationDetailAction.OnPickDocumentUpload(pickedFile,category.documentTypeId.orEmpty()))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { action(ApplicationDetailAction.OnExpand(category.documentTypeId.orEmpty()))},
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = DocumentTheme.CardBackground),
        border = BorderStroke(1.dp, DocumentTheme.CardBorder)
    ) {
        Column {
            // ── Header row ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DocumentIconTile(status = category.overallStatus)
                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = category.documentTypeName.orEmpty(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DocumentTheme.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    DocumentStatusRow(
                        status = category.overallStatus,
                        fileCount = category.uploadedDocuments.size
                    )
                }

                Spacer(Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (category.isMultipleAllowed == true) MultiBadge()
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = DocumentTheme.TextSecondary,
                        modifier = Modifier.size(22.dp).rotate(chevronAngle)
                    )
                }
            }

            // ── Expanded body ──
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(300, easing = EaseInOutCubic)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(280, easing = EaseInOutCubic)) + fadeOut(tween(200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DocumentTheme.ExpandedSurface)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = DocumentTheme.DividerColor, thickness = 2.dp)

                    category.uploadedDocuments.forEach { file ->
                        key(file.applicationDocumentId) {
                            FileRow(
                                file = file,
                                category.showDltIcon,
                                onDelete = {
                                    action(ApplicationDetailAction.OnDeleteDocument(file))
                                })
                        }
                    }

                    if(category.canDocumentUpload) {
                        AddFileButton(
                            label = if (category.uploadedDocuments.isEmpty()) "Add file" else "+ Add another file",
                            onClick = { pickPdf() }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(9.dp))
                                .background(Color(0xFFFFF0F0))
                                .border(1.5.dp, Color(0xFFD4A0A0), RoundedCornerShape(9.dp))
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category.documentUploadErrorMsg,
                                fontSize = 13.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF9E4A4A),
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = 8.sp,
                                    maxFontSize = 13.sp,
                                    stepSize = 1.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun DocumentIconTile(status: DocumentCategoryState) {
    val bg = when (status) {
        DocumentCategoryState.VERIFIED -> DocumentTheme.IconBgVerified
        DocumentCategoryState.IN_PROGRESS -> DocumentTheme.IconBgInProgress
        DocumentCategoryState.PENDING -> DocumentTheme.IconBgPending
    }
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFFFF3F3)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = Res.getUri("files/ic_doc.svg"),
            contentDescription = "Document Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun DocumentStatusRow(status: DocumentCategoryState, fileCount: Int) {
    val (label, color) = when (status) {
        DocumentCategoryState.VERIFIED -> "Verified" to DocumentTheme.Verified
        DocumentCategoryState.IN_PROGRESS -> "In progress" to DocumentTheme.InProgress
        DocumentCategoryState.PENDING -> "Pending" to DocumentTheme.Pending
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(5.dp))
        Text(text = label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium)
        if (fileCount > 0) {
            Text(
                text = " · $fileCount file${if (fileCount > 1) "s" else ""}",
                fontSize = 11.sp,
                color = DocumentTheme.TextSecondary
            )
        }
    }
}

@Composable
fun MultiBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE8F0FF))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Multi", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2C5CC5))
    }
}

@Composable
fun FileRow(file: UploadedDocument, showDltIcon : Boolean, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(DocumentTheme.CardBackground)
            .border(1.dp, DocumentTheme.FileTileBorder, RoundedCornerShape(9.dp))
            .padding(horizontal = 11.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = Res.getUri("files/ic_file.svg"),
            contentDescription = "Document Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = file.fileName.orEmpty(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = DocumentTheme.TextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(8.dp))
        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFFFEEEE))
                .clickable(onClick = {
                    if (showDltIcon) {
                        onDelete()
                    }
                }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (showDltIcon) Icons.Default.Delete else Icons.Default.Lock,
                contentDescription = "Delete file",
                tint = DocumentTheme.Primary,
                modifier = Modifier.size(16.dp)
            )
        }

    }
}

@Composable
fun AddFileButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(Color(0xFFFFF0F0))
            .border(1.dp, DocumentTheme.Primary, RoundedCornerShape(9.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DocumentTheme.Primary
        )
    }
}
@file:Suppress("DEPRECATION")

package org.getscol.gscol.auth.presentation.registration.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.and_text
import scol.composeapp.generated.resources.i_agree_the
import scol.composeapp.generated.resources.privacy_policy
import scol.composeapp.generated.resources.terms_of_service

@Composable
fun TermsAndPrivacyCheckBox(
    isTermsAccepted: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isTermsAccepted,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.error
            )
        )

        val annotatedText = buildAnnotatedString {
            append(stringResource(Res.string.i_agree_the))

            pushStringAnnotation(tag = "TERMS", annotation = "terms")
            pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            )
            append(stringResource(Res.string.terms_of_service))
            pop()
            pop()

            append(stringResource(Res.string.and_text))

            pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
            pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            )
            append(stringResource(Res.string.privacy_policy))
            pop()
            pop()
        }

        ClickableText(
            text = annotatedText,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
            onClick = { offset ->
                annotatedText
                    .getStringAnnotations(offset, offset)
                    .firstOrNull()
                    ?.let {
                        when (it.tag) {
                            "TERMS" -> onTermsClick()
                            "PRIVACY" -> onPrivacyClick()
                        }
                    }
            }
        )
    }
}
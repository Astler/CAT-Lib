package dev.astler.ui.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit

@Composable
fun EnchantedAutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    centered: Boolean = false,
    isBold: Boolean = false,
    textColor: Color? = null,
    minTextSize: TextUnit = TextUnit.Unspecified,
    maxTextSize: TextUnit = TextUnit.Unspecified,
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    AutoSizeText(
        modifier = modifier.fillMaxWidth(),
        text = text,
        minTextSize = minTextSize,
        maxTextSize = maxTextSize,
        style = if (centered) typography.bodyMedium else typography.bodyLarge,
        color = textColor ?: colors.onSurface,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        alignment = if (centered) Alignment.Center else Alignment.TopStart,
    )
}

@Preview(widthDp = 200, heightDp = 100)
@Preview(widthDp = 200, heightDp = 30)
@Preview(widthDp = 60, heightDp = 30)
@Preview(widthDp = 20, heightDp = 50)
@Composable
fun EnchantedAutoSizeTextPreview() {
    MaterialTheme {
        Surface {
            EnchantedAutoSizeText(
                text = "OvTracker App",
                modifier = Modifier,
            )
        }
    }
}
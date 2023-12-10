package dev.astler.unlib_compose.ui.compose.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.astler.unlib_compose.theme.GoogleSans

@Composable
fun EnchantedTitle(
    modifier: Modifier = Modifier,
    textSize: Float = 20f,
    @StringRes text: Int,
    centered: Boolean = false
) {
    EnchantedTitle(modifier, stringResource(id = text), textSize, centered)
}

@Composable
fun EnchantedTitle(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Float = 20f,
    centered: Boolean = false,
    color: Color? = null
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    EnchantedText(
        modifier = modifier,
        textStyle = if (centered) typography.titleLarge else typography.titleMedium.copy(fontSize = textSize.sp),
        text = text,
        textSize = textSize,
        centered = centered,
        isBold = true,
        color = color ?: colors.tertiary
    )
}

@Composable
fun EnchantedText(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    textSize: Float = 18f,
    centered: Boolean = false,
    isBold: Boolean = false,
    textStyle: TextStyle? = null,
    color: Color? = null

) {
    EnchantedText(modifier, stringResource(id = text), textSize, centered, isBold, textStyle, color)
}

@Composable
fun EnchantedText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Float = 18f,
    centered: Boolean = false,
    isBold: Boolean = false,
    textStyle: TextStyle? = null,
    color: Color? = null
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Text(
        text = text,
        style = textStyle ?: (if (centered) typography.bodyMedium else typography.bodyLarge),
        fontFamily = GoogleSans,
        fontSize = textSize.sp,
        color = color ?: colors.onSurface,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        modifier = modifier
    )
}
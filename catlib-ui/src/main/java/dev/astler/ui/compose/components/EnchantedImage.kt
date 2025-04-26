package dev.astler.ui.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.astler.ui.extensions.thenIf

@Composable
fun ImageWithShadow(
    model: Any?,
    modifier: Modifier,
    tintColor: Color? = null,
    backgroundContent: @Composable (() -> Unit)? = null,
    foregroundContent: @Composable (() -> Unit)? = null,
    showShadow: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = modifier) {
        val colors = MaterialTheme.colorScheme

        backgroundContent?.invoke()

        if (showShadow) {
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 6.dp, top = 8.dp, end = 5.dp, bottom = 4.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(colors.onSurface.copy(alpha = 0.1f))
            )
        }

        AsyncImage(
            model = model,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(ShapeDefaults.Medium)
                .padding(2.dp)
                .thenIf(onClick != null, Modifier.clickable { onClick?.invoke() })
                .padding(4.dp),
            contentScale = ContentScale.Fit,
            colorFilter = tintColor?.let { ColorFilter.tint(it) }
        )

        foregroundContent?.invoke()
    }
}
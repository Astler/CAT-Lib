package dev.astler.ui.compose.components

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

@Composable
fun ComposeImageView(
    modifier: Modifier = Modifier,
    drawableId: Int,
    colorFilter: Int? = null,
) {
    Box(modifier = modifier) {
        AndroidView(factory = { viewContext ->
            AppCompatImageView(viewContext).apply {
                val drawable = ContextCompat.getDrawable(viewContext, drawableId)
                this.background = drawable
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                colorFilter?.let {
                    setColorFilter(it)
                }
            }
        }
        )
    }
}
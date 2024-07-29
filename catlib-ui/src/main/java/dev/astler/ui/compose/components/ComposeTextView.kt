package dev.astler.ui.compose.components

import android.graphics.Paint
import android.os.Build
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import dev.astler.ui.utils.getAttributeColor
import dev.astler.ui.views.LinksCompatTextView
import dev.astler.catlib.core.R

@Composable
fun ComposeTextView(
    modifier: Modifier = Modifier,
    text: CharSequence?,
    textSize: Float = 18f,
    isUnderlined: Boolean = false,
    isCentered: Boolean = false,
    isBold: Boolean = false,
    allowLinks: Boolean = false,
    textColor: Color? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    val context = LocalContext.current
    val typeface = remember { ResourcesCompat.getFont(context, if (isBold) R.font.google_sans_bold else R.font.google_sans_reg) }

    Box(modifier = modifier.wrapContentHeight()) {
        AndroidView(factory = { viewContext ->
            LinksCompatTextView(viewContext).apply {
                this.text = text

                if (isCentered)
                    gravity = Gravity.CENTER

                this.textSize = textSize

                if (isUnderlined)
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                if (isCentered)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER

                this.maxLines = maxLines

                ellipsize = if (maxLines == Int.MAX_VALUE) null else TextUtils.TruncateAt.END

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                this.typeface = typeface

                if (textColor != null)
                    setTextColor(textColor.toArgb())
                else
                    setTextColor(context.getAttributeColor(com.google.android.material.R.attr.colorOnSurface))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    focusable = View.NOT_FOCUSABLE
                }

                isClickable = allowLinks
                linksClickable = allowLinks
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
            update = { view ->
                view.text = text
            }
        )
    }
}
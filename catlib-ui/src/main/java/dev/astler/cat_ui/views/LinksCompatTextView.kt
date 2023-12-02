package dev.astler.cat_ui.views

import android.content.Context
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import dev.astler.cat_ui.views.CatTextView

class LinksCompatTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : CatTextView(context, attrs, defStyleAttr) {

    private var clickableSpan: ClickableSpan? = null

    init {
        movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Extract the text (as Spanned)
        val stext = text as Spanned

        // Get the clicked position
        var x = event?.x?.toInt() ?: -1
        var y = event?.y?.toInt() ?: -1

        x -= totalPaddingLeft
        y -= totalPaddingTop

        x += scrollX
        y += scrollY

        val layout = layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        // Check if there's a clickable span under the click
        val link = stext.getSpans(off, off, ClickableSpan::class.java)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (link.isNotEmpty()) {
                    clickableSpan = link[0]
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                clickableSpan?.onClick(this)
                clickableSpan = null
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                clickableSpan = null
            }
        }

        // Otherwise, let the framework handle the touch event (this won't consume it for non-clickable text)
        return false
    }
}

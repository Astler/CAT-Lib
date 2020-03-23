package dev.astler.library.view

import android.text.Spannable
import android.content.Context
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import dev.astler.library.view.style.VerticalImageSpan
import dev.astler.unli.PreferencesTool
import dev.astler.unli.view.PrefsTextView
import java.util.regex.Pattern

class TextViewWithImages(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        PrefsTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(
            context,
            attrs,
            android.R.attr.textViewStyle
    ) {
        initText(attrs)
    }

    constructor(context: Context) : this(context, null) {
        initText(null)
    }

    override fun setText(text: CharSequence, type: BufferType) {
        val s = getTextWithImages(context, text)
        super.setText(s, BufferType.SPANNABLE)
    }

    companion object {

        private val spannableFactory = Spannable.Factory.getInstance()

        private fun addImages(context: Context, spannable: Spannable): Boolean {
            val refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E")

            var hasChanges = false

            val matcher = refImg.matcher(spannable)

            while (matcher.find()) {
                  var set = true
                for (span in spannable.getSpans(matcher.start(), matcher.end(), ImageSpan::class.java)) {
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end()) {
                        spannable.removeSpan(span)
                    } else {
                        set = false
                        break
                    }
                }
                val resname = spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim { it <= ' ' }

                val id = context.resources.getIdentifier(resname, "drawable", context.packageName)

                if (id != 0) {
                    if (set) {
                        hasChanges = true

                        val prefsTool = PreferencesTool(context)

                        val d = ContextCompat.getDrawable(context, id)

                        val size = (prefsTool.textSize * 3).toInt()

                        d?.setBounds(0, 0, size, size)
                        val imageSpan = VerticalImageSpan(d!!)

                        spannable.setSpan(
                            imageSpan,
                            matcher.start(),
                            matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }

            return hasChanges
        }

        private fun getTextWithImages(context: Context, text: CharSequence): Spannable {
            val spannable = spannableFactory.newSpannable(text)
            addImages(context, spannable)
            return spannable
        }
    }
}
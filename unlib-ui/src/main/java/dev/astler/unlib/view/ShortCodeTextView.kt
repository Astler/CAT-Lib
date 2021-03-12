package dev.astler.unlib.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.text.toSpannable
import dev.astler.unlib.ui.R
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.utils.getDrawableByName
import dev.astler.unlib.view.span.CustomTypefaceSpan
import java.util.regex.Pattern

class ShortCodeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) :
    PrefsTextView(context, attrs, defStyleAttr) {

    companion object {
        private val spannableFactory = Spannable.Factory.getInstance()
    }

    private var iconSize = 18f

    init {
        initText(attrs)

        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.PrefsTextView, 0, 0)

        val textSizeModifier =
            typedArray.getInteger(R.styleable.PrefsTextView_textSizeModifier, 0)
        iconSize = gPreferencesTool.mTextSize + textSizeModifier
    }

    override fun setText(text: CharSequence, type: BufferType) {
        val s = replaceShortCodesWithImages(context, text)
        super.setText(s, BufferType.SPANNABLE)
    }

    private fun replaceShortCodesWithImages(context: Context, text: CharSequence): Spannable {
        val spannable = addDecorations(spannableFactory.newSpannable(text))

        addImages(context, spannable)
        return spannable
    }

    private fun addDecorations(pSpannable: Spannable): Spannable {
        var innerSpan = pSpannable

        val boldPattern = Pattern.compile("\\Q[bold text=\\E([a-zA-Z0-9А-Яа-яё.,:@_ ]+?)\\Q/]\\E")

        var matcher = boldPattern.matcher(innerSpan)

        while (matcher.find()) {
            var set = true

            for (span in innerSpan.getSpans(matcher.start(), matcher.end(), CustomTypefaceSpan::class.java)) {
                if (innerSpan.getSpanStart(span) >= matcher.start() && innerSpan.getSpanEnd(span) <= matcher.end()) {
                    innerSpan.removeSpan(span)
                } else {
                    set = false
                    break
                }
            }

            if (set) {
                val resToBold = innerSpan.subSequence(matcher.start(1), matcher.end(1)).toString().trim { it <= ' ' }

                val sb = SpannableStringBuilder(innerSpan)

                sb.replace(matcher.start(), matcher.end(), resToBold)

                val typefaceSpan = CustomTypefaceSpan(ResourcesCompat.getFont(context, R.font.google_sans_bold))

                sb.setSpan(typefaceSpan,
                    matcher.start(), matcher.end() - (13),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                innerSpan = sb.toSpannable()

                matcher = boldPattern.matcher(sb)
            }
        }

        return innerSpan
    }

    private fun addImages(context: Context, pSpannable: Spannable) {
        val nMatcher = Pattern
            .compile("\\Q[img src=\\E([a-zA-Z0-9._]+?)(?:(?: tint=([a-zA-Z0-9#._]+?)\\Q/]\\E)|(?:\\Q/]\\E))")
            .matcher(pSpannable)

        while (nMatcher.find()) {
            var set = true
            for (span in pSpannable.getSpans(
                nMatcher.start(),
                nMatcher.end(),
                ImageSpan::class.java
            )) {
                if (pSpannable.getSpanStart(span) >= nMatcher.start() && pSpannable.getSpanEnd(span) <= nMatcher.end()) {
                    pSpannable.removeSpan(span)
                } else {
                    set = false
                    break
                }
            }

            val nImageResourceName =
                pSpannable.subSequence(nMatcher.start(1), nMatcher.end(1)).toString()
                    .trim { it <= ' ' }

            val nTintColor = if (nMatcher.group(2) != null) {
                pSpannable.subSequence(nMatcher.start(2), nMatcher.end(2)).toString()
                    .trim { it <= ' ' }
            } else ""

            if (set) {
                context.getDrawableByName(nImageResourceName)?.let {
                    val nSize = (iconSize * context.resources.displayMetrics.density).toInt()

                    val nBitmapImg = Bitmap.createScaledBitmap(it.toBitmap(), nSize, nSize, false)
                    val nDrawable = nBitmapImg?.toDrawable(context.resources)

                    if (nTintColor.isNotEmpty())
                        nDrawable?.let { bitmapDrawable ->
                            DrawableCompat.setTint(bitmapDrawable, Color.parseColor(nTintColor))
                        }

                    nDrawable?.setBounds(0, 0, nSize, nSize)

                    nDrawable?.let {
                        val imageSpan = VerticalImageSpan(nDrawable)

                        pSpannable.setSpan(
                            imageSpan,
                            nMatcher.start(),
                            nMatcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }
    }
}
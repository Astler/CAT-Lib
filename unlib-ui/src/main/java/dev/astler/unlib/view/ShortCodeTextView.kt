package dev.astler.unlib.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.text.toSpannable
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.R
import dev.astler.unlib.utils.getDrawableByName
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.view.span.CustomFancyTextSpan
import java.util.regex.Pattern

open class ShortCodeTextView @JvmOverloads constructor(
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

    override fun setText(pText: CharSequence, type: BufferType) {
        super.setText(replaceShortCodes(context, spannableFactory.newSpannable(pText)), BufferType.SPANNABLE)
    }

    open fun replaceShortCodes(context: Context, pSpannable: Spannable): Spannable {
        var nSpannable = pSpannable

        if (nSpannable.contains("[ft"))
            nSpannable = addFancyText(nSpannable)

        if (nSpannable.contains("[img"))
            addImages(context, nSpannable)

        return nSpannable
    }

    private fun addFancyText(pSpannable: Spannable): Spannable {
        var innerSpan = pSpannable

        val nPattern = Pattern
            .compile("\\Q[ft text=\\E([a-zA-Z0-9А-Яа-яё{}/.,:@_ ]+?)(?:(?: color=([a-zA-Z0-9#._]+?))|(?:))(?:(?: params=([~a-zA-Z0-9#._]+?))|(?:))/]\\Q\\E")

        var nMatcher = nPattern.matcher(innerSpan)

        while (nMatcher.find()) {
            var set = true

            for (
                span in innerSpan.getSpans(
                    nMatcher.start(),
                    nMatcher.end(),
                    CustomFancyTextSpan::class.java
                )
            ) {
                if (innerSpan.getSpanStart(span) >= nMatcher.start() && innerSpan.getSpanEnd(span) <= nMatcher.end()) {
                    innerSpan.removeSpan(span)
                } else {
                    set = false
                    break
                }
            }

            if (set) {
                val resToBold = innerSpan.subSequence(nMatcher.start(1), nMatcher.end(1)).toString()
                    .trim { it <= ' ' }
                val nSecond = if (nMatcher.group(2) != null) innerSpan.subSequence(
                    nMatcher.start(2),
                    nMatcher.end(2)
                ).toString().trim { it <= ' ' } else ""
                val nThird = if (nMatcher.group(3) != null) innerSpan.subSequence(
                    nMatcher.start(3),
                    nMatcher.end(3)
                ).toString().trim { it <= ' ' } else ""

                val nColorRaw = when {
                    nSecond.startsWith("#") -> {
                        nSecond
                    }
                    nThird.startsWith("#") -> {
                        nThird
                    }
                    else -> {
                        ""
                    }
                }

                val nColor = if (nColorRaw.isEmpty()) -1 else Color.parseColor(nColorRaw)

                var nParamsRaw = when {
                    nSecond.startsWith("~") -> {
                        nSecond
                    }
                    nThird.startsWith("~") -> {
                        nThird
                    }
                    else -> {
                        ""
                    }
                }

                val nParamsMap = HashMap<String, String>()

                if (nParamsRaw.isNotEmpty()) {
                    nParamsRaw = nParamsRaw.replaceFirst("~", "")

                    val nParamsArray = nParamsRaw.split("#")

                    nParamsArray.forEach {
                        val nData = it.split("_")

                        if (nData.size == 2) {
                            nParamsMap[nData[0]] = nData[1]
                        }
                    }
                }

                val nRemoveChars = 11 +
                    (if (nColor != -1) 7 + nColorRaw.length else 0) +
                    (if (nParamsRaw.isNotEmpty()) 9 + nParamsRaw.length else 0) +
                    (if (nParamsMap.containsKey("s.after")) -(nParamsMap["s.after"] ?: "0").toInt() else 0)

                val nTypeface = if (nParamsMap.containsKey("tf")) {
                    getTypefaceByParams(nParamsMap["tf"] ?: "")
                } else null

                val sb = SpannableStringBuilder(innerSpan)

                sb.replace(nMatcher.start(), nMatcher.end(), resToBold)

                sb.setSpan(
                    CustomFancyTextSpan(nTypeface, nColor),
                    nMatcher.start(), nMatcher.end() - nRemoveChars,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                innerSpan = sb.toSpannable()

                nMatcher = nPattern.matcher(innerSpan)
            }
        }

        return innerSpan
    }

    open fun getTypefaceByParams(pKey: String): Typeface? {
        return when (pKey) {
            "b" -> ResourcesCompat.getFont(context, R.font.google_sans_bold)
            // "m" -> ResourcesCompat.getFont(context, R.font.minecraft)
            else -> null
        }
    }

    open fun addImages(context: Context, pSpannable: Spannable) {
        val nMatcher = Pattern
            .compile("\\Q[img src=\\E([a-zA-Z0-9._]+?)(?:(?: tint=([a-zA-Z0-9#._]+?)\\Q/]\\E)|(?:\\Q/]\\E))")
            .matcher(pSpannable)

        while (nMatcher.find()) {
            var set = true
            for (
                span in pSpannable.getSpans(
                    nMatcher.start(),
                    nMatcher.end(),
                    ImageSpan::class.java
                )
            ) {
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
                    var nSize = (iconSize * context.resources.displayMetrics.density).toInt()

                    if (nSize <= 0) {
                        infoLog("Error! Bitmap size = $nSize")
                        nSize = 1
                    }

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

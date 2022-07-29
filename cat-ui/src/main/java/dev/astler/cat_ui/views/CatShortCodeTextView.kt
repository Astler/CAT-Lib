package dev.astler.cat_ui.views

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.toSpannable
import dev.astler.cat_ui.R
import dev.astler.cat_ui.utils.getDrawableByName
import dev.astler.cat_ui.views.custom.VerticalImageSpan
import dev.astler.cat_ui.views.span.CustomFancyTextSpan
import dev.astler.unlib.gPreferencesTool
import kotlinx.coroutines.*
import java.util.regex.Pattern

open class CatShortCodeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) :
    CatTextView(context, attrs, defStyleAttr) {

    companion object {
        private val spannableFactory = Spannable.Factory.getInstance()
    }

    private var emptyDrawable: Drawable? = null

    private var _spannableData: Spannable? = null
    protected val spannableData: Spannable get() = _spannableData!!

    protected var iconSize = 18f
    protected var scope: CoroutineScope? = null

    open val emptyIconId: Int = R.drawable.ic_splash_logo

    init {
        if (scope == null) {
            scope = CoroutineScope(Job() + Dispatchers.Main)
        }

        initText(attrs)

        freezesText = true

        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CatTextView, 0, 0)

        val textSizeModifier =
            typedArray.getInteger(R.styleable.CatTextView_textSizeModifier, 0)

        iconSize = if (!isInEditMode) gPreferencesTool.mTextSize else 18f + textSizeModifier

        emptyDrawable = ContextCompat.getDrawable(context, emptyIconId)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        _spannableData = null
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("height", height)

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            height = state.getInt("height")
            super.onRestoreInstanceState(state.getParcelable("superState"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun setText(pText: CharSequence, type: BufferType) {
        if (scope == null) {
            scope = CoroutineScope(Job() + Dispatchers.Main)
        }

        if (pText.isEmpty() && _spannableData.isNullOrEmpty()) {
            super.setText(pText, BufferType.NORMAL)
            return
        }

        if (pText.contains("[") || pText.contains("]")) {
            scope?.launch(Dispatchers.IO) {
                val nText = processShortCodes(context, spannableFactory.newSpannable(pText))

                withContext(Dispatchers.Main) {
                    super.setText(nText, BufferType.SPANNABLE)
                }
            }
        } else {
            super.setText(pText, BufferType.NORMAL)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope?.cancel()
    }

    protected fun showSpannableText() {
        setText(spannableData, BufferType.SPANNABLE)
    }

    protected fun updateSpannable(spannable: Spannable) {
        _spannableData = spannable
    }

    protected fun getSpannable() = _spannableData

    open suspend fun processShortCodes(context: Context, pSpannable: Spannable): Spannable {
        if (!getSpannable().isNullOrEmpty()) return spannableData

        updateSpannable(pSpannable)

        if (spannableData.contains("[ft"))
            addFancyText()

        if (spannableData.contains("[img"))
            addImages(context)

        return spannableData
    }

    open fun getTypefaceByParams(pKey: String): Typeface? {
        return when (pKey) {
            "b" -> ResourcesCompat.getFont(context, R.font.google_sans_bold)
            // "m" -> ResourcesCompat.getFont(context, R.font.minecraft)
            else -> null
        }
    }

    protected open suspend fun addFancyText() {
        val nPattern = Pattern
            .compile("\\Q[ft text=\\E([a-zA-Z\\dА-Яа-яё{}/.,=:@_ ]+?)(?:(?: color=([a-zA-Z\\d#._]+?))|(?:))(?:(?: params=([~a-zA-Z\\d#._]+?))|(?:))/]\\Q\\E")

        var nMatcher = nPattern.matcher(spannableData)

        while (nMatcher.find()) {
            var set = true

            for (
            span in spannableData.getSpans(
                nMatcher.start(),
                nMatcher.end(),
                CustomFancyTextSpan::class.java
            )
            ) {
                if (spannableData.getSpanStart(span) >= nMatcher.start() && spannableData.getSpanEnd(
                        span
                    ) <= nMatcher.end()
                ) {
                    spannableData.removeSpan(span)
                } else {
                    set = false
                    break
                }
            }

            if (set) {
                val resToBold =
                    spannableData.subSequence(nMatcher.start(1), nMatcher.end(1)).toString()
                        .trim { it <= ' ' }
                val nSecond = if (nMatcher.group(2) != null) spannableData.subSequence(
                    nMatcher.start(2),
                    nMatcher.end(2)
                ).toString().trim { it <= ' ' } else ""
                val nThird = if (nMatcher.group(3) != null) spannableData.subSequence(
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
                        (if (nParamsMap.containsKey("s.after")) -(nParamsMap["s.after"]
                            ?: "0").toInt() else 0)

                val nTypeface = if (nParamsMap.containsKey("tf")) {
                    getTypefaceByParams(nParamsMap["tf"] ?: "")
                } else null

                val sb = SpannableStringBuilder(spannableData)

                sb.replace(nMatcher.start(), nMatcher.end(), resToBold)

                sb.setSpan(
                    CustomFancyTextSpan(nTypeface, nColor),
                    nMatcher.start(), nMatcher.end() - nRemoveChars,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                _spannableData = sb.toSpannable()

                nMatcher = nPattern.matcher(spannableData)
            }
        }
    }

    protected open fun addImages(context: Context) {
        scope?.launch(Dispatchers.IO) {
            val nMatcher = Pattern
                .compile("\\Q[img src=\\E([a-zA-Z\\d._]+?)(?:(?: tint=([a-zA-Z\\d#._]+?)\\Q/]\\E)|(?:\\Q/]\\E))")
                .matcher(spannableData)

            while (nMatcher.find()) {
                var set = true
                for (
                span in spannableData.getSpans(
                    nMatcher.start(),
                    nMatcher.end(),
                    ImageSpan::class.java
                )
                ) {
                    if (spannableData.getSpanStart(span) >= nMatcher.start() && spannableData.getSpanEnd(
                            span
                        ) <= nMatcher.end()
                    ) {
                        spannableData.removeSpan(span)
                    } else {
                        set = false
                        break
                    }
                }

                val nImageResourceName =
                    spannableData.subSequence(nMatcher.start(1), nMatcher.end(1)).toString()
                        .trim { it <= ' ' }

                val nTintColor = if (nMatcher.group(2) != null) {
                    spannableData.subSequence(nMatcher.start(2), nMatcher.end(2)).toString()
                        .trim { it <= ' ' }
                } else ""

                if (set) {
                    val nSize = (iconSize * context.resources.displayMetrics.density).toInt()

                    setImageSpanInPosition(
                        emptyDrawable,
                        nTintColor,
                        nSize,
                        nMatcher.start(),
                        nMatcher.end()
                    )

                    getShortCodeDrawable(nImageResourceName) {
                        setImageSpanInPosition(
                            it,
                            nTintColor,
                            nSize,
                            nMatcher.start(),
                            nMatcher.end()
                        )
                    }
                }
            }

            withContext(Dispatchers.Main) {
                showSpannableText()
            }
        }
    }

    protected open suspend fun getShortCodeDrawable(
        pName: String,
        onComplete: (Drawable?) -> Unit
    ) {
        context.getDrawableByName(pName)?.let {
            onComplete(it)
        }
    }

    protected open fun setImageSpanInPosition(
        drawable: Drawable?,
        tintColor: String,
        size: Int,
        start: Int,
        end: Int
    ) {
        if (drawable == null) return

        if (tintColor.isNotEmpty())
            DrawableCompat.setTint(drawable, Color.parseColor(tintColor))

        drawable.setBounds(0, 0, size, size)

        val imageSpan = VerticalImageSpan(drawable)

//        spannableData.getSpans<VerticalImageSpan>(start, end).forEach {
//            spannableData.removeSpan(it)
//        }

        spannableData.setSpan(
            imageSpan,
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

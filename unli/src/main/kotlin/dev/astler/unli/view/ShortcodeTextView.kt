package dev.astler.unli.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import dev.astler.unli.PreferencesTool
import dev.astler.unli.R
import dev.astler.unli.view.style.VerticalImageSpan
import java.util.regex.Pattern

class ShortcodeTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        PrefsTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(
            context,
            attrs,
            android.R.attr.textViewStyle
    ) {
        initText(attrs)
        iconSizeInit(attrs)
    }

    private var iconSize = 0f

    constructor(context: Context) : this(context, null) {
        initText(null)
        iconSizeInit(null)
    }

    private fun iconSizeInit(attrs: AttributeSet?) {
        val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.PrefsTextView, 0, 0)

        val textSizeModifier =
                typedArray.getInteger(R.styleable.PrefsTextView_textSizeModifier, 0)
        iconSize = PreferencesTool(context).textSize + textSizeModifier
    }

    override fun setText(text: CharSequence, type: BufferType) {
        val s = getTextWithImages(context, text)
        super.setText(s, BufferType.SPANNABLE)
    }

    companion object {
        private val spannableFactory = Spannable.Factory.getInstance()
    }

    private fun addImages(context: Context, pSpannable: Spannable): Boolean {
        val refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9._]+?)\\Q/]\\E")

        val innerSpan = pSpannable

        var hasChanges = false

        val matcher = refImg.matcher(innerSpan)

        while (matcher.find()) {
            var set = true
            for (span in innerSpan.getSpans(matcher.start(), matcher.end(), ImageSpan::class.java)) {
                if (innerSpan.getSpanStart(span) >= matcher.start() && innerSpan.getSpanEnd(span) <= matcher.end()) {
                    innerSpan.removeSpan(span)
                } else {
                    set = false
                    break
                }
            }

            val resname = innerSpan.subSequence(matcher.start(1), matcher.end(1)).toString().trim { it <= ' ' }
            val id = context.resources.getIdentifier(resname, "drawable", context.packageName)

            if (set) {
                hasChanges = true

                val d = if (id != 0) ContextCompat.getDrawable(context, id)
                else {
                    try {
                        Drawable.createFromResourceStream(
                                context.resources,
                                TypedValue(), context.resources.assets.open("$resname.png"), null
                        )
                    } catch (e: Exception) {
                        ContextCompat.getDrawable(context, R.drawable.file)
                    }
                }

                val displayMetrics = context.resources.displayMetrics

                val size = (iconSize * displayMetrics.density).toInt()

                d?.setBounds(0, 0, size, size)
                val imageSpan = VerticalImageSpan(d!!)

                innerSpan.setSpan(imageSpan,
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

            }
        }
        return hasChanges
    }

    private fun addTintImages(context: Context, pSpannable: Spannable): Boolean {
        val refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9._]+?)\\Q tint=\\E([a-zA-Z0-9#._]+?)\\Q/]\\E")

        var hasChanges = false

        val matcher = refImg.matcher(pSpannable)

        while (matcher.find()) {
            var set = true
            for (span in pSpannable.getSpans(matcher.start(), matcher.end(), ImageSpan::class.java)) {
                if (pSpannable.getSpanStart(span) >= matcher.start() && pSpannable.getSpanEnd(span) <= matcher.end()) {
                    pSpannable.removeSpan(span)
                } else {
                    set = false
                    break
                }
            }

            val resname = pSpannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim { it <= ' ' }
            val nTintColor = pSpannable.subSequence(matcher.start(2), matcher.end(2)).toString().trim { it <= ' ' }

            val id = context.resources.getIdentifier(resname, "drawable", context.packageName)

            if (set) {
                hasChanges = true

                val d = if (id != 0) ContextCompat.getDrawable(context, id)
                else {
                    try {
                        Drawable.createFromResourceStream(
                                context.resources,
                                TypedValue(), context.resources.assets.open("$resname.png"), null
                        )
                    } catch (e: Exception) {
                        ContextCompat.getDrawable(context, R.drawable.file)
                    }
                }

                d?.let {
                    val displayMetrics = context.resources.displayMetrics

                    val size = (iconSize * displayMetrics.density).toInt()

                    val nBitmapImg = Bitmap.createScaledBitmap(it.toBitmap(), size, size, false)

                    val nDrawable = nBitmapImg?.toDrawable(context.resources)

                    nDrawable?.let { bitmapDrawable ->
                        DrawableCompat.setTint(bitmapDrawable, Color.parseColor(nTintColor))
                    }

                    nDrawable?.setBounds(0, 0, size, size)

                    val imageSpan = VerticalImageSpan(nDrawable!!)

                    pSpannable.setSpan(imageSpan,
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
        addTintImages(context, spannable)
        return spannable
    }

    class ClickSpanItem(val start: Int, val end: Int, val clickData: String, val clickName: String)
    class BoldSpanItem(val start: Int, val end: Int)
}
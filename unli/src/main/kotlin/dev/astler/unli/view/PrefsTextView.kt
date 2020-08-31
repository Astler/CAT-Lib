package dev.astler.unli.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import dev.astler.unli.R
import dev.astler.unli.preferencesTool
import dev.astler.unli.utils.getColorFromAttr

open class PrefsTextView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        initText(attrs)
    }

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

    fun initText(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.PrefsTextView, 0, 0)

        val useBold = typedArray.getBoolean(R.styleable.PrefsTextView_useBoldFont, false)

        typeface = ResourcesCompat.getFont(context, R.font.sans_font_family)

        if (useBold)
            setTypeface(null, Typeface.BOLD)
        else
            setTypeface(null, Typeface.NORMAL)

        textSize = preferencesTool.textSize + typedArray.getInteger(R.styleable.PrefsTextView_textSizeModifier, 0)

        if (typedArray.getBoolean(R.styleable.PrefsTextView_changeTextColor, true)) {
            setTextColor(context.getColorFromAttr(R.attr.contrastColorByTheme))
        }
    }

    fun setThemeColor() {
        setTextColor(context.getColorFromAttr(R.attr.contrastColorByTheme))
    }

    fun setBoldTypeface() {
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)
    }

    fun setRegTypeface() {
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_reg)
    }

}
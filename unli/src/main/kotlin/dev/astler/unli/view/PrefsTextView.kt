package dev.astler.unli.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import dev.astler.unli.PreferencesTool
import dev.astler.unli.R
import dev.astler.unli.utils.getColorFromAttr

open class PrefsTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
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

        val typeface = if (useBold) ResourcesCompat.getFont(context, R.font.google_sans_bold)
        else ResourcesCompat.getFont(context, R.font.google_sans_reg)

        setTypeface(typeface)

        val textSizeModifier =
            typedArray.getInteger(R.styleable.PrefsTextView_textSizeModifier, 0)
        textSize = PreferencesTool(context).textSize + textSizeModifier

        val changeColor = typedArray.getBoolean(R.styleable.PrefsTextView_changeTextColor, true)

        if (changeColor) {
            setTextColor(context.getColorFromAttr(R.attr.contrastColorByTheme))
        }
    }

    fun setThemeColor() {
        setTextColor(context.getColorFromAttr(R.attr.contrastColorByTheme))
    }

}
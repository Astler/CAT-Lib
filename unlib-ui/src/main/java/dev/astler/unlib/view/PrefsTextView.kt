package dev.astler.unlib.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.R
import dev.astler.unlib.utils.getColorFromAttr

open class PrefsTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        initText(attrs)
    }

    fun initText(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.PrefsTextView, 0, 0)

        textSize = gPreferencesTool.mTextSize + typedArray.getInteger(R.styleable.PrefsTextView_textSizeModifier, 0)

        when (typedArray.getInteger(R.styleable.PrefsTextView_textStyle, 10)) {
            11 -> {
                setBoldTypeface()
            }
            else -> {
                setRegTypeface()
            }
        }

        if (typedArray.getBoolean(R.styleable.PrefsTextView_themeTextColor, true)) {
            defaultPrefsTextColor()
        }
    }

    fun defaultPrefsTextColor() {
        setTextColor(context.getColorFromAttr(R.attr.contrastColorByTheme))
    }

    fun setBoldTypeface() {
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)
    }

    fun setRegTypeface() {
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_reg)
    }
}

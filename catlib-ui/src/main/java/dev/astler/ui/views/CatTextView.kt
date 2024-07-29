package dev.astler.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ui.R

open class CatTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        initText(attrs)
    }

    fun initText(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CatTextView, 0, 0)

        //TODO do smth with this..
        textSize = (if (!isInEditMode) PreferencesTool(context).textSize else 18f) + typedArray.getInteger(
            R.styleable.CatTextView_textSizeModifier,
            0
        )

        setTextStyle(typedArray.getInteger(R.styleable.CatTextView_textStyle, 10))
    }

    /**
     * @param styleId Defines font to use (10 - regular, 11 - bold, 12 - italic)
     *
     * @attr ref android.R.styleable#CatTextView_textStyle
     */

    fun setTextStyle(styleId: Int) {
        typeface = when (styleId) {
            11 -> {
                ResourcesCompat.getFont(context, dev.astler.catlib.core.R.font.google_sans_bold)
            }

            else -> {
                ResourcesCompat.getFont(context, dev.astler.catlib.core.R.font.google_sans_reg)
            }
        }
    }
}

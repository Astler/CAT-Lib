package dev.astler.cat_ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import dev.astler.cat_ui.R
import dev.astler.catlib.gPreferencesTool

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

        textSize = (if (!isInEditMode) gPreferencesTool.mTextSize else 18f) + typedArray.getInteger(
            R.styleable.CatTextView_textSizeModifier,
            0
        )

        typeface = when (typedArray.getInteger(R.styleable.CatTextView_textStyle, 10)) {
            11 -> {
                ResourcesCompat.getFont(context, R.font.google_sans_bold)
            }

            else -> {
                ResourcesCompat.getFont(context, R.font.google_sans_reg)
            }
        }
    }
}

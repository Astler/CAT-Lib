package dev.astler.cat_ui.views.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.graphics.PathParser
import dev.astler.cat_ui.R
import dev.astler.cat_ui.utils.dpToPixels
import dev.astler.cat_ui.utils.getAttributeColor
import dev.astler.cat_ui.utils.isAppDarkTheme
import dev.astler.cat_ui.utils.special.VectorDrawableParser
import dev.astler.cat_ui.utils.views.getContextColor

class SplashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundColor = context.getAttributeColor(R.attr.backgroundColorByTheme)

    private val iconPaint: Paint = Paint().apply {
        isAntiAlias = true

        color = getContextColor(
            if (!isInEditMode) {
                if (context.isAppDarkTheme())
                    R.color.white
                else R.color.default_background_dark
            } else {
                R.color.default_background_dark
            }
        )

        style = Paint.Style.FILL
    }

    private val maskPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = backgroundColor
        style = Paint.Style.FILL
        alpha = 255
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private val logoWidthHeight: Float
    private val viewportWidthHeight: Float

    private val originalIconPath: Path?
    private val iconPath: Path = Path()
    private val iconMatrix: Matrix = Matrix()

    private var scale = 1f

    private var logoSideHalf: Int = 0
    private var scaledLogoWidthHeight: Int = 0

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        VectorDrawableParser.parsedVectorDrawable(resources, R.drawable.ic_splash_logo).run {
            originalIconPath = this?.pathData?.let { PathParser.createPathFromPathData(it) }

            logoWidthHeight = dpToPixels(this?.width ?: 0f)
            viewportWidthHeight = this?.viewportWidth ?: 0f
        }
    }

    fun animateLogo() {
        ValueAnimator.ofFloat(MASK_SCALE_START, MASK_SCALE_END).apply {
            startDelay = SCALE_ANIMATION_START_DELAY
            duration = SCALE_ANIMATION_DURATION
            interpolator = AccelerateInterpolator()

            addUpdateListener {
                scale = it.animatedValue as Float
                requestLayout()
                postInvalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    this@SplashView.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                    this@SplashView.visibility = GONE
                }
            })

            start()
        }

        ValueAnimator.ofInt(255, 1).apply {
            duration = ALPHA_ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener { maskPaint.alpha = it.animatedValue as Int }
            start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        scaledLogoWidthHeight = (scale * logoWidthHeight).toInt()
        logoSideHalf = scaledLogoWidthHeight / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(backgroundColor)

        canvas.drawCircle(width / 2f, height / 2f, MASK_FINAL_RADIUS * this.scale, maskPaint)

        drawLogo(
            canvas,
            scaledLogoWidthHeight,
            width / 2 - logoSideHalf,
            height / 2 - logoSideHalf
        )
    }

    private fun drawLogo(canvas: Canvas, widthHeight: Int, dx: Int, dy: Int) {
        originalIconPath ?: return

        val scale = widthHeight / viewportWidthHeight

        canvas.save()
        canvas.translate(
            (widthHeight - scale * viewportWidthHeight) / 2f + dx,
            (widthHeight - scale * viewportWidthHeight) / 2f + dy
        )
        iconMatrix.reset()
        iconMatrix.setScale(scale, scale)
        canvas.scale(1.0f, 1.0f)
        iconPath.reset()
        iconPath.addPath(originalIconPath)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()
    }

    companion object {
        private const val MASK_FINAL_RADIUS = 180f

        private const val MASK_SCALE_START = 1f
        private const val MASK_SCALE_END = 75f

        private const val SCALE_ANIMATION_START_DELAY = 750L
        private const val SCALE_ANIMATION_DURATION = 1500L

        private const val ALPHA_ANIMATION_DURATION = 2000L
    }
}

package dev.astler.cat_ui.utils.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dev.astler.unlib.utils.isR

fun View.setStatusPaddingForView(pAdditionalTopPadding: Int = 0) {
    setStatusAndNavigationPaddingForView(pBottomPadding = false, pAdditionalTopPadding = pAdditionalTopPadding)
}

fun View.setNavigationPaddingForView(pAdditionalBottomPadding: Int = 0) {
    setStatusAndNavigationPaddingForView(pTopPadding = false, pAdditionalBottomPadding = pAdditionalBottomPadding)
}

@Suppress("DEPRECATION")
fun View.setStatusAndNavigationPaddingForView(
    pTopPadding: Boolean = true,
    pBottomPadding: Boolean = true,
    pAdditionalTopPadding: Int = 0,
    pAdditionalBottomPadding: Int = 0,
    pAngle: Int = 0
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        v.updatePadding(
            top = if (pTopPadding) {
                if (isR()) {
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top + pAdditionalTopPadding
                } else {
                    insets.systemWindowInsetTop + pAdditionalTopPadding
                }
            } else 0,
            bottom = when (pAngle) {
                90 -> 0
                -90 -> 0
                else -> {
                    if (pBottomPadding) {
                        if (isR()) {
                            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom + pAdditionalBottomPadding
                        } else {
                            insets.systemWindowInsetBottom + pAdditionalBottomPadding
                        }
                    } else 0
                }
            },
            left = when (pAngle) {
                90 -> {
                    if (pBottomPadding) {
                        if (isR()) {
                            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).left
                        } else {
                            insets.systemWindowInsetLeft
                        }
                    } else 0
                }
                -90 -> 0
                else -> 0
            },
            right = when (pAngle) {
                90 -> 0
                -90 -> {
                    if (pBottomPadding) {
                        if (isR()) {
                            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).right
                        } else {
                            insets.systemWindowInsetRight
                        }
                    } else 0
                }
                else -> 0
            }
        )

        insets
    }
}

@Suppress("DEPRECATION")
fun View.setBottomMarginInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val params = v.layoutParams as ViewGroup.MarginLayoutParams

        params.bottomMargin = if (isR()) {
            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        } else {
            insets.systemWindowInsetBottom
        }

        v.layoutParams = params
        insets
    }
}

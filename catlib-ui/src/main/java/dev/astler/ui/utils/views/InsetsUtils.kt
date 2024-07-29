package dev.astler.ui.utils.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.setStatusPaddingForView(pAdditionalTopPadding: Int = 0) {
    setStatusAndNavigationPaddingForView(
        pBottomPadding = false,
        pAdditionalTopPadding = pAdditionalTopPadding
    )
}

fun View.setNavigationPaddingForView(pAdditionalBottomPadding: Int = 0) {
    setStatusAndNavigationPaddingForView(
        pTopPadding = false,
        pAdditionalBottomPadding = pAdditionalBottomPadding
    )
}

fun View.setStatusAndNavigationPaddingForView(
    pTopPadding: Boolean = true,
    pBottomPadding: Boolean = true,
    pAdditionalTopPadding: Int = 0,
    pAdditionalBottomPadding: Int = 0,
    pAngle: Int = 0
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        v.updatePadding(
            top = if (pTopPadding) insets.getInsets(WindowInsetsCompat.Type.systemBars()).top + pAdditionalTopPadding else 0,
            bottom = when (pAngle) {
                90 -> 0
                -90 -> 0
                else -> {
                    if (pBottomPadding) insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom + pAdditionalBottomPadding else 0
                }
            },
            left = when (pAngle) {
                90 -> {
                    if (pBottomPadding) insets.getInsets(WindowInsetsCompat.Type.navigationBars()).left else 0
                }
                -90 -> 0
                else -> 0
            },
            right = when (pAngle) {
                90 -> 0
                -90 -> {
                    if (pBottomPadding) insets.getInsets(WindowInsetsCompat.Type.navigationBars()).right else 0
                }
                else -> 0
            }
        )

        insets
    }
}

fun View.setBottomMarginInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val params = v.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        v.layoutParams = params
        insets
    }
}

fun View.setInsetsPadding(topInsets: Int = 0, bottomInsets: Int = 0, angle: Int = 0) {
    updatePadding(
        top = topInsets,
        bottom = when (angle) {
            90 -> 0
            -90 -> 0
            else -> bottomInsets
        },
        left = when (angle) {
            90 -> bottomInsets
            -90 -> 0
            else -> 0
        },
        right = when (angle) {
            90 -> 0
            -90 -> bottomInsets
            else -> 0
        }
    )
}
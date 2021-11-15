package dev.astler.unlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.* // ktlint-disable no-wildcard-imports
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.clickDialog(@StringRes pDialogMsgId: Int) {
    setOnClickListener {
        context.dialog(pMsg = context.getString(pDialogMsgId))
    }
}

fun View.clickDialog(pDialogMsg: String) {
    setOnClickListener {
        context.dialog(pMsg = pDialogMsg)
    }
}

fun ImageView.setColorTintDrawable(@DrawableRes icon: Int, @ColorRes colorId: Int) {
    setImageDrawable(context.tintDrawable(icon, colorId))
}

fun ImageView.setAttrTintDrawable(@DrawableRes icon: Int, @AttrRes attrId: Int) {
    setImageDrawable(context.tintDrawableByAttr(icon, attrId))
}

fun RecyclerView.hideFABOnScroll(fab: FloatingActionButton) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0)
                fab.hide()
            else if (dy < 0)
                fab.show()
        }
    })
}

fun NestedScrollView.hideFABOnScroll(fab: FloatingActionButton) {
    setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
        if (scrollY > oldScrollY) {
            fab.hide()
        } else {
            fab.show()
        }
    }
}

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.goneView() {
    visibility = View.GONE
}

fun View.showViewWithCondition(pCondition: Boolean) {
    if (pCondition) {
        showView()
    } else {
        goneView()
    }
}

fun Context.simpleTextChip(@StringRes pTextId: Int): View {
    return simpleTextChip(getString(pTextId))
}

fun Context.simpleTextChip(pText: String): View {
    val mChip = Chip(this)
    mChip.text = pText

    return mChip
}

fun View.getCaptureBitmap(): Bitmap? {
    var screenshot: Bitmap? = null
    try {
        screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        draw(canvas)
    } catch (e: Exception) {
        infoLog("UNLIB: Failed to capture screenshot because:" + e.message)
    }

    return screenshot
}

fun View.getContextColor(@ColorRes pColorId: Int): Int {
    return context.getContextColor(pColorId)
}

fun ViewGroup.initView(@LayoutRes layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

/**
 * Padding Functions For Status And Navigation Bars
 */

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

fun View.okDialog(
    pTitle: String = "",
    pMsg: String = "",
    pOkAction: () -> Unit = {},
    pInstantDialog: Boolean = true
): AlertDialog = context.okDialog(pTitle, pMsg, pOkAction, pInstantDialog)

package dev.astler.unlib.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * TODO Узнать что сделать с Deprecated FLAG_TRANSLUCENT_STATUS!
 */

fun Activity.setSystemBarColor(@ColorRes color: Int) {
    if (isL()) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nDecorView = window.decorView
            val wic = WindowInsetsControllerCompat(window, nDecorView)

            wic.isAppearanceLightStatusBars = false
        }

        window.statusBarColor = ContextCompat.getColor(this, color)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

fun Activity.setSystemBarTransparent() {
    if (isL()) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

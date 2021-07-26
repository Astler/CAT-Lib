package dev.astler.unlib.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Some items from: https://github.com/FunkyMuse/KAHelpers/blob/master/activity/src/main/java/com/crazylegend/activity/ActivityExtensions.kt
 */

/**
 * Status Bar Block
 * TODO Узнать что сделать с Deprecated FLAG_TRANSLUCENT_STATUS!
 */

fun Activity.setStatusBarColor(@ColorRes color: Int) {
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

val Activity.getStatusBarHeight: Int
    get() {
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        return rect.top
    }

fun Activity.brightness(brightness: Float = 1f) {
    val params = window.attributes
    params.screenBrightness = brightness // range from 0 - 1 as per docs
    window.attributes = params
    window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED)
}

fun Activity.lockOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
}

fun Activity.lockCurrentScreenOrientation() {
    requestedOrientation = when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }
}

fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

fun Activity.setBackgroundColor(@ColorInt color: Int) {
    window.setBackgroundDrawable(ColorDrawable(color))
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun Activity.showKeyboard(pView: View) {
    if (pView.requestFocus()) {
        val inputMethod =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.showSoftInput(pView, InputMethodManager.SHOW_IMPLICIT)
    }
}

// Находим View с фокусом, так мы сможем получить правильный window token
// Если такого View нет, то создадим одно, это для получения window token из него
fun Activity.hideKeyboard() {

    val view = currentFocus ?: View(this)
    val inputMethod =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(
        view.windowToken,
        InputMethodManager.SHOW_IMPLICIT
    )
}

fun Context.hideKeyboardFrom(view: View?) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun AppCompatActivity.setupToolbar(
    toolbar: Toolbar,
    displayHomeAsUpEnabled: Boolean = true,
    displayShowHomeEnabled: Boolean = true,
    displayShowTitleEnabled: Boolean = false,
    showUpArrowAsCloseIcon: Boolean = false,
    @DrawableRes closeIconDrawableRes: Int? = null
) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
        setDisplayShowHomeEnabled(displayShowHomeEnabled)
        setDisplayShowTitleEnabled(displayShowTitleEnabled)

        if (showUpArrowAsCloseIcon && closeIconDrawableRes != null) {
            setHomeAsUpIndicator(
                AppCompatResources.getDrawable(
                    this@setupToolbar,
                    closeIconDrawableRes
                )
            )
        }
    }
}

fun Activity.keepScreenOn() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun Activity.keepScreenOFF() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun Activity.screenShot(removeStatusBar: Boolean = false): Bitmap? {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)

    val bmp = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.RGB_565)
    val canvas = Canvas(bmp)
    window.decorView.draw(canvas)

    return if (removeStatusBar) {
        val statusBarHeight = statusBarHeight
        Bitmap.createBitmap(
            bmp,
            0,
            statusBarHeight,
            dm.widthPixels,
            dm.heightPixels - statusBarHeight
        )
    } else {
        Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels)
    }
}

private val Activity.statusBarHeight: Int
    get() {
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(id)
    }

/**
 * Наверное буду использовать только для тестов
 */

fun AppCompatActivity.getDisplayDensity(): String {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return when (metrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "LDPI"
        DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
        DisplayMetrics.DENSITY_HIGH -> "HDPI"
        DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
        DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
        else -> "XXHDPI"
    }
}

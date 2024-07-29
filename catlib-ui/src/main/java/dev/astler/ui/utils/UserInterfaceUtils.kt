package dev.astler.ui.utils

import android.app.UiModeManager
import android.content.Context
import android.graphics.Color
import kotlin.math.roundToInt

val Context.isSystemDarkMode: Boolean
    get() = (this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager).nightMode == UiModeManager.MODE_NIGHT_YES

fun Context.isAppDarkTheme(currentMode: String): Boolean {
    return ((currentMode == "auto" || currentMode == "system") && isSystemDarkMode) || currentMode == "dark"
}

fun Int.toHexColor() = String.format("#%06X", 0xFFFFFF and this)

fun Int.setAlpha(alphaPercent: Float): Int {
    val alpha = (Color.alpha(this) * alphaPercent).roundToInt()
    return this and 0x00FFFFFF or (alpha shl 24)
}
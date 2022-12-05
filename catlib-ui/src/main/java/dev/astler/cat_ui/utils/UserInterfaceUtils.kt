package dev.astler.cat_ui.utils

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate.* 
import dev.astler.catlib.gPreferencesTool
import kotlin.math.roundToInt

fun Int.toHexColor(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}

val Context.isSystemDarkMode
    get() = if (getDefaultNightMode() == MODE_NIGHT_FOLLOW_SYSTEM)
        resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    else getDefaultNightMode() == MODE_NIGHT_YES

fun Context.isAppDarkTheme(): Boolean {
    return gPreferencesTool.mIsDarkTheme || gPreferencesTool.mIsSystemTheme && isSystemDarkMode
}

fun Int.setAlpha(alphaPercent: Float): Int {
    val alpha = (Color.alpha(this) * alphaPercent).roundToInt()
    return this and 0x00FFFFFF or (alpha shl 24)
}
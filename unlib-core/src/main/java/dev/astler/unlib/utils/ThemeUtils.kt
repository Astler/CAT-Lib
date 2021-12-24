package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.gPreferencesTool

val Context.isSystemDarkMode
    get() = if (getDefaultNightMode() == MODE_NIGHT_FOLLOW_SYSTEM)
        resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    else getDefaultNightMode() == MODE_NIGHT_YES

fun Context.isAppDarkTheme(): Boolean {
    return gPreferencesTool.mIsDarkTheme || gPreferencesTool.mIsSystemTheme && isSystemDarkMode
}

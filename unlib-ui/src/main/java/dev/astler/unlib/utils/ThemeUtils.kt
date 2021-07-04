package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Configuration
import dev.astler.unlib.gPreferencesTool

fun Context.isSystemDarkTheme(): Boolean {
    return resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.isAppDarkTheme(): Boolean {
    return gPreferencesTool.mIsDarkTheme || gPreferencesTool.mIsSystemTheme && isSystemDarkTheme()
}

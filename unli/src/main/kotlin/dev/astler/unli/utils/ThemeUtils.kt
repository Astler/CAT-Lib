package dev.astler.unli.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import dev.astler.unli.preferencesTool

fun Context.isSystemDarkTheme(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.isAppDarkTheme(): Boolean {
    return preferencesTool.isDarkTheme || preferencesTool.isSystemTheme && isSystemDarkTheme()
}
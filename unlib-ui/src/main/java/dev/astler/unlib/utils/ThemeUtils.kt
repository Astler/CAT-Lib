package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Configuration
import dev.astler.unlib.UnliApp
import dev.astler.unlib.cDark
import dev.astler.unlib.cSystemDefault

fun Context.isSystemDarkTheme(): Boolean {
    return resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.isAppDarkTheme(): Boolean {
    return UnliApp.getInstance().mAppTheme == cDark || UnliApp.getInstance().mAppTheme == cSystemDefault && isSystemDarkTheme()
}

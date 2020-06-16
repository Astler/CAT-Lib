package dev.astler.unli.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import dev.astler.unli.preferencesTool

class ThemeUtils {

    companion object {
        fun getDefaultNightMode() = when (preferencesTool.appTheme) {
            "light" -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            "dark" -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else -> if (isQ()) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }
    }
}

fun isDarkTheme(activity: Activity): Boolean {
    return activity.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
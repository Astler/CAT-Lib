package dev.astler.catlib.extensions

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import dev.astler.catlib.preferences.PreferencesTool

val PreferencesTool.defaultNightMode
    get() = when (appTheme) {
        "light" -> AppCompatDelegate.MODE_NIGHT_NO
        "dark" -> AppCompatDelegate.MODE_NIGHT_YES
        else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
    }
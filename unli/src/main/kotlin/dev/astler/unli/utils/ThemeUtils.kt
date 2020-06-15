package dev.astler.unli.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dev.astler.unli.UtilsX
import dev.astler.unli.preferencesTool

class ThemeUtils {

    companion object {
        fun getDefaultNightMode(context: Context) = when (preferencesTool.appTheme) {
            "light" -> {
                UtilsX.log("MODE_NIGHT_NO")
                AppCompatDelegate.MODE_NIGHT_NO
            }
            "dark" -> {
                UtilsX.log("MODE_NIGHT_YES")
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else -> if (VersioningUtils.isQ()) {
                UtilsX.log("MODE_NIGHT_FOLLOW_SYSTEM")
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                UtilsX.log("MODE_NIGHT_AUTO_BATTERY")
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }
    }
}
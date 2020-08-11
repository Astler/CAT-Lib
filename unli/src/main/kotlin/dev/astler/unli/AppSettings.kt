package dev.astler.unli

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import dev.astler.unli.utils.LocalizationUtil
import dev.astler.unli.utils.log
import java.util.*

class AppSettings {

    companion object {

        private fun loadLocalePref(): String {
            return if(preferencesTool.chooseLanguageManually) {
                preferencesTool.userLanguage
            } else Locale.getDefault().language
        }

        @Suppress("deprecation")
        fun loadLocale(context: Context, englishMode: Boolean = false): Context? {
            return LocalizationUtil.applyLanguage(context, if (englishMode) Locale.ENGLISH else Locale(loadLocalePref()))
        }
    }
}
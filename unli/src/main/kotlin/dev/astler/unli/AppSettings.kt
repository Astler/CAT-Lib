package dev.astler.unli

import android.content.Context
import dev.astler.unli.utils.updateLocale
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
            return updateLocale(context, if (englishMode) Locale.ENGLISH else Locale(loadLocalePref()))
        }
    }
}
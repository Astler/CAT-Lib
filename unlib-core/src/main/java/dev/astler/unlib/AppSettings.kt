package dev.astler.unlib

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import dev.astler.unlib.utils.infoLog
import java.util.*

/**
 * Отвечает за выбор языка, работает вроде как нормально, но бывают кейсы без обработки
 */
class AppSettings {

    companion object {
        private fun loadLocalePref(): String {
            return if(gPreferencesTool.isSystemLanguage) {
                ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).toString()
            } else gPreferencesTool.appLanguage
        }

        @Suppress("deprecation")
        fun loadLocale(context: Context): Context? {

            val customLocale: Locale = localeForLanguageTag(loadLocalePref())

            Locale.setDefault(customLocale)

            val resources: Resources = context.resources
            val config: Configuration = resources.configuration

            val nNewContext = if (Build.VERSION.SDK_INT >= 25) {
                context.createConfigurationContext(config)
            } else {
                context
            }

            if (Build.VERSION.SDK_INT >= 19) config.setLocale(customLocale) else config.locale = customLocale

            val metrics: DisplayMetrics = resources.displayMetrics

            resources.updateConfiguration(config, metrics)

            return nNewContext
        }

        private fun localeForLanguageTag(languageTag: String): Locale {
            infoLog(languageTag)

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Locale.forLanguageTag(languageTag.replace("_".toRegex(), "-"))
            } else {
                val parts = languageTag.split("[_]").toTypedArray()
                val language = parts[0]
                val country = if (parts.size >= 2) parts[1] else null
                country?.let { Locale(language, it) } ?: Locale(language)
            }
        }
    }
}
package dev.astler.unli

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import java.util.*

class AppSettings {

    companion object {

        private fun loadLocalePref(context: Context?): String {
            val nPreferencesTool = PreferencesTool(context)

            return if(nPreferencesTool.chooseLanguageManually()) {
                nPreferencesTool.getUserLanguage()
            } else ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).toString()
        }

        @Suppress("deprecation")
        fun loadLocale(context: Context): Context? {
            val languageTag = loadLocalePref(context)
            val customLocale: Locale = Locale.ENGLISH //localeForLanguageTag(languageTag)

            UtilsX.log("tag: $languageTag :: locale: $customLocale")
            UtilsX.log(
                "system language ${ConfigurationCompat.getLocales(Resources.getSystem().configuration)
                    .get(0)}"
            )

            Locale.setDefault(customLocale)

            val resources: Resources = context.applicationContext.resources
            val config: Configuration = resources.configuration

            if (Build.VERSION.SDK_INT >= 19) config.setLocale(customLocale) else config.locale = customLocale

            return if (Build.VERSION.SDK_INT >= 25) {
                ContextWrapper(context.createConfigurationContext(config))
            } else {
                val metrics: DisplayMetrics = resources.displayMetrics
                resources.updateConfiguration(config, metrics)
                ContextWrapper(context)
            }
        }

        private fun localeForLanguageTag(languageTag: String): Locale {
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
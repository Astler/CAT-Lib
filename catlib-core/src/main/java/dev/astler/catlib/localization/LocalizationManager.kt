package dev.astler.catlib.localization

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import gg.pressf.resources.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalizationManager @Inject constructor() {

    private val supportedLanguages: Map<String, String> by lazy {
        mapOf(
            SYSTEM_LANGUAGE to "System",
            "en" to "English",
            "ar" to "العربية",
            "de" to "Deutsch",
            "es" to "Español",
            "fr" to "Français",
            "id" to "Bahasa Indonesia",
            "ja" to "日本語",
            "ko" to "한국어",
            "pl" to "Polski",
            "pt" to "Português",
            "pt-BR" to "Português (Brasil)",
            "ru" to "Русский",
            "uk" to "Українська",
            "zh" to "中文"
        )
    }

    companion object {
        const val SYSTEM_LANGUAGE = "system"

        fun getSystemLanguage(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales[0].language
            } else {
                @Suppress("DEPRECATION")
                Resources.getSystem().configuration.locale.language
            }
        }
    }

    fun isLanguageSupported(languageCode: String): Boolean {
        return languageCode == SYSTEM_LANGUAGE || supportedLanguages.containsKey(languageCode)
    }

    fun setAppLocale(languageCode: String) {
        when {
            languageCode == SYSTEM_LANGUAGE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                } else {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.getAdjustedDefault())
                }
            }

            supportedLanguages.containsKey(languageCode) -> {
                val localeList = LocaleListCompat.forLanguageTags(languageCode)
                AppCompatDelegate.setApplicationLocales(localeList)
            }

            else -> {
                val localeList = LocaleListCompat.forLanguageTags("en")
                AppCompatDelegate.setApplicationLocales(localeList)
            }
        }
    }

    fun getCurrentLanguage(): String {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        return if (currentLocales.isEmpty) {
            SYSTEM_LANGUAGE
        } else {
            currentLocales[0]?.language ?: SYSTEM_LANGUAGE
        }
    }

    fun getDisplayName(languageCode: String, context: Context): String {
        return when (languageCode) {
            SYSTEM_LANGUAGE -> {
                try {
                    context.getString(R.string.system)
                } catch (e: Exception) {
                    "System"
                }
            }

            else -> supportedLanguages[languageCode] ?: languageCode
        }
    }
}
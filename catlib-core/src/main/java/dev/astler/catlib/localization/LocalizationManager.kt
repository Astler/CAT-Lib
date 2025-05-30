package dev.astler.catlib.localization

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dev.astler.catlib.preferences.PreferencesTool
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalizationManager @Inject constructor(
    private val preferences: PreferencesTool
) {
    companion object {
        private const val DEFAULT_LOCALE = "system"
    }

    /**
     * Получить текущую локаль приложения
     */
    fun getCurrentLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - используем AppCompatDelegate
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            if (currentLocales.isEmpty) {
                DEFAULT_LOCALE
            } else {
                currentLocales[0]?.toLanguageTag() ?: DEFAULT_LOCALE
            }
        } else {
            // Android < 13 - используем SharedPreferences
            preferences.appLanguage
        }
    }

    /**
     * Установить локаль приложения
     * @param languageTag - код языка (например, "en", "ru", "uk" или "system" для системной)
     * @param context - контекст для обновления конфигурации на старых версиях Android
     */
    fun setLocale(languageTag: String, context: Context? = null) {
        preferences.appLanguage = languageTag

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - используем современный API
            setLocaleModern(languageTag)
        } else {
            // Android < 13 - используем legacy подход
            setLocaleLegacy(languageTag, context)
        }
    }

    /**
     * Современный способ для Android 13+
     */
    private fun setLocaleModern(languageTag: String) {
        val localeList = if (languageTag == DEFAULT_LOCALE) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageTag)
        }

        // Это автоматически пересоздаст активность
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    /**
     * Legacy способ для Android < 13
     */
    private fun setLocaleLegacy(languageTag: String, context: Context?) {
        context?.let { ctx ->
            val locale = if (languageTag == DEFAULT_LOCALE) {
                getSystemLocale()
            } else {
                Locale.forLanguageTag(languageTag)
            }

            updateContextLocale(ctx, locale)
        }
    }

    /**
     * Получить системную локаль
     */
    private fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            android.content.res.Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            android.content.res.Resources.getSystem().configuration.locale
        }
    }

    /**
     * Обновить контекст с новой локалью (для старых версий Android)
     */
    private fun updateContextLocale(context: Context, locale: Locale) {
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
        }

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    /**
     * Проверить, нужно ли инициализировать локаль при старте приложения
     */
    fun initializeLocaleOnStart(context: Context) {
        val savedLocale = preferences.appLanguage

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && savedLocale != DEFAULT_LOCALE) {
            setLocaleLegacy(savedLocale, context)
        }
    }

    /**
     * Проверить, поддерживается ли современный API локализации
     */
    fun isModernLocalizationSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}
package dev.astler.unlib_compose.data.settings

import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.gPreferencesTool
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SettingsImpl @Inject constructor() : Settings {

    override val themeStream: MutableStateFlow<AppTheme>
    override val languageStream: MutableStateFlow<AppLanguage>
    override var theme: AppTheme by AppThemePreferenceDelegate()
    override var language: AppLanguage by AppLanguagePreferenceDelegate()

    init {
        themeStream = MutableStateFlow(theme)
        languageStream = MutableStateFlow(language)
    }

    inner class AppThemePreferenceDelegate : ReadWriteProperty<Any?, AppTheme> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): AppTheme =
            AppTheme.fromString(gPreferencesTool.getString(PreferencesTool.appThemeKey, AppTheme.SYSTEM.name.lowercase()))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppTheme) {
            themeStream.value = value
            gPreferencesTool.edit(PreferencesTool.appThemeKey, value.name.lowercase())
        }
    }

    inner class AppLanguagePreferenceDelegate : ReadWriteProperty<Any?, AppLanguage> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): AppLanguage =
            AppLanguage.fromString(gPreferencesTool.getString(PreferencesTool.appLocaleKey, AppLanguage.SYSTEM.name.lowercase()))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppLanguage) {
            languageStream.value = value
            gPreferencesTool.edit(PreferencesTool.appLocaleKey, value.name.lowercase())
        }
    }
}

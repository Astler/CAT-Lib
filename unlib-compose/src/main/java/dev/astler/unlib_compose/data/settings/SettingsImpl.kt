package dev.astler.unlib_compose.data.settings

import dev.astler.catlib.preferences.PreferencesTool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SettingsImpl @Inject constructor(val preferencesTool: PreferencesTool) : Settings {

    override val themeStream: MutableStateFlow<AppTheme>
    override val materialTheme: StateFlow<Boolean>
    override val languageStream: MutableStateFlow<AppLanguage>
    override var theme: AppTheme by AppThemePreferenceDelegate()
    override var language: AppLanguage by AppLanguagePreferenceDelegate()

    init {
        themeStream = MutableStateFlow(theme)
        languageStream = MutableStateFlow(language)
        materialTheme = MutableStateFlow(true)
    }

    inner class AppThemePreferenceDelegate : ReadWriteProperty<Any?, AppTheme> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): AppTheme =
            AppTheme.fromString(preferencesTool.getString(PreferencesTool.appThemeKey, AppTheme.SYSTEM.name.lowercase()))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppTheme) {
            themeStream.value = value
            preferencesTool.edit(PreferencesTool.appThemeKey, value.name.lowercase())
        }
    }

    inner class AppLanguagePreferenceDelegate : ReadWriteProperty<Any?, AppLanguage> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): AppLanguage =
            AppLanguage.fromString(preferencesTool.getString(PreferencesTool.appLocaleKey, AppLanguage.SYSTEM.name.lowercase()))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppLanguage) {
            languageStream.value = value
            preferencesTool.edit(PreferencesTool.appLocaleKey, value.name.lowercase())
        }
    }
}

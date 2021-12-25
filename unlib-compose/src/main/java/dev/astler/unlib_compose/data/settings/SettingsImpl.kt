package dev.astler.unlib_compose.data.settings

import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.gPreferencesTool
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SettingsImpl @Inject constructor() : Settings {

    override val themeStream: MutableStateFlow<AppTheme>
    override var theme: AppTheme by AppThemePreferenceDelegate(PreferencesTool.appThemeKey, AppTheme.SYSTEM)

    init {
        themeStream = MutableStateFlow(theme)
    }

    inner class AppThemePreferenceDelegate(
        private val name: String,
        private val default: AppTheme,
    ) : ReadWriteProperty<Any?, AppTheme> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): AppTheme =
            AppTheme.fromString(gPreferencesTool.getString(name, default.name.lowercase()))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppTheme) {
            themeStream.value = value

            gPreferencesTool.edit(name, value.name.lowercase())
        }
    }
}

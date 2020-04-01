package dev.astler.unli

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*

open class PreferencesTool(context: Context?) {

    companion object {

        fun newInstance(context: Context) = PreferencesTool(context)

        const val appThemeKey = "appTheme"

        const val appLocaleKey = "appLocale"
        const val appLocaleDefault = "system"
        const val appLocaleModeKey = "appLocaleMode"
        const val appLocaleModeDefault = false

        const val textSizeKey = "textSizeNew"
        const val textSizeDefault = "18"

        const val useEnglishKey = "useEnglish"
        const val firstStartKey = "firstStart"
        const val dayWithoutAdsKey = "dayWithoutAds"
    }

    private var mPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    open fun loadDefaultPreferences(context: Context) {
        PreferenceManager.setDefaultValues(context, R.xml.prefs, false)
    }

    fun getPreferences(): SharedPreferences = mPreferences

    var textSize: Float
        get() = (getPreferences().getString(textSizeKey, textSizeDefault))?.toFloat()?:18f
        set(value) {
            edit(textSizeKey, value.toString())
        }

    var isDarkTheme: Boolean
        get() = appTheme == "dark"
        set(value) {
            edit(appThemeKey, "dark")
        }

    var isSystemTheme: Boolean
        get() = appTheme == "system"
        set(value) {
            if (value)
                edit(appThemeKey, "system")
        }

    var appTheme: String
        get() = getPreferences().getString(appThemeKey, "system") ?: "system"
        set(value) {
            edit(appThemeKey, value)
        }

    var useEnglish: Boolean
        get() = getPreferences().getBoolean(useEnglishKey, false)
        set(useEnglish) {
            edit(useEnglishKey, useEnglish)
        }

    var isFirstStart: Boolean
        get() = getPreferences().getBoolean(firstStartKey, true)
        set(value) {
            edit(firstStartKey, value)
        }

    var dayWithoutAds: Int
        get() = getPreferences().getInt(dayWithoutAdsKey, -1)
        set(value) {
            edit(dayWithoutAdsKey, value)
        }

    var chooseLanguageManually: Boolean
        get() = getPreferences().getBoolean(appLocaleModeKey, appLocaleModeDefault)
        set(value) {
            edit(appLocaleModeKey, value)
        }

    var userLanguage: String
        get() = getPreferences().getString(appLocaleKey, appLocaleDefault) ?: appLocaleDefault
        set(value) {
            edit(appLocaleKey, value)
        }

    fun isFirstStartForVersion(versionCode: Int) =
        getPreferences().getBoolean("firstStartVersion$versionCode", true)

    fun addListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        getPreferences().registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        getPreferences().unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun setFirstStartForVersion(versionCode: Int) {
        edit("firstStartVersion$versionCode", false)
    }

    open fun preferencesToDefault() {
        isSystemTheme = true
        textSize = 18f
        useEnglish = false
        chooseLanguageManually = false
        userLanguage = "en"
    }

    fun edit(name: String, type: Any) {
        val editor = getPreferences().edit()

        when (type) {
            is Boolean -> editor.putBoolean(name, type)
            is Int -> editor.putInt(name, type)
            is Float -> editor.putFloat(name, type)
            is String -> editor.putString(name, type)
        }

        editor.apply()
    }
}

val Context.appPrefs: PreferencesTool get() = PreferencesTool.newInstance(this)
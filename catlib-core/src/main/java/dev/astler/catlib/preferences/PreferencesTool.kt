package dev.astler.catlib.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import androidx.preference.PreferenceManager
import dev.astler.catlib.core.R
import javax.inject.Inject

open class PreferencesTool @Inject constructor(context: Context) {

    companion object {
        const val appThemeKey = "appTheme"
        const val vibrationKey = "keyVibration"

        const val cNoAdsName = "noAdsPref"

        const val appLocaleKey = "appLocale"
        const val appLocaleDefault = "system"

        const val textSizeKey = "textSizeNew"
        const val textSizeDefault = "18"

        const val firstStartKey = "firstStart"
        const val dayWithoutAdsKey = "dayWithoutAds"

        const val appFirstStartTimeKey = "appFirstStartTime"
    }

    private var mPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    open fun loadDefaultPreferences(
        context: Context,
        pNewPreferencesArray: IntArray = intArrayOf()
    ) {
        PreferenceManager.setDefaultValues(context, R.xml.prefs, false)

        pNewPreferencesArray.forEach {
            PreferenceManager.setDefaultValues(context, it, false)
        }
    }

    private fun getPreferences(): SharedPreferences = mPreferences

    var appFirstStartTime: Long
        get() = getLong(appFirstStartTimeKey, 0L)
        set(value) {
            edit(appFirstStartTimeKey, value)
        }

    var textSize: Float
        get() = (getPreferences().getString(textSizeKey, textSizeDefault))?.toFloat() ?: 18f
        set(value) {
            edit(textSizeKey, value.toString())
        }

    var vibrateOnClick: Boolean
        get() = getPreferences().getBoolean(vibrationKey, false)
        set(value) {
            if (value)
                edit(vibrationKey, "system")
        }

    var adsDisabled: Boolean
        get() = getPreferences().getBoolean(cNoAdsName, false)
        set(value) {
            edit(cNoAdsName, value)
        }

    var isSystemLanguage: Boolean
        get() = appLanguage == "system"
        set(value) {
            if (value)
                edit(appLocaleKey, "system")
        }

    var appTheme: String
        get() = getPreferences().getString(appThemeKey, "system") ?: "system"
        set(value) {
            edit(appThemeKey, value)
        }

    var appLanguage: String
        get() = getPreferences().getString(appLocaleKey, "system") ?: "system"
        set(value) {
            edit(appLocaleKey, value)
        }

    var isFirstStart: Boolean
        get() = getPreferences().getBoolean(firstStartKey, true)
        set(value) {
            edit(firstStartKey, value)
        }

    var noAdsDay: Int
        get() = getPreferences().getInt(dayWithoutAdsKey, -1)
        set(value) {
            edit(dayWithoutAdsKey, value)
        }

    var userLanguage: String
        get() = getPreferences().getString(appLocaleKey, appLocaleDefault) ?: appLocaleDefault
        set(value) {
            edit(appLocaleKey, value)
        }

    var appReviewTime: Long
        get() = getLong("appReviewTime", 0L)
        set(value) {
            edit("appReviewTime", value)
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
        textSize = 18f
        userLanguage =
            ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).toString()
    }

    fun edit(name: String, type: Any) {
        val editor = getPreferences().edit()

        when (type) {
            is Boolean -> editor.putBoolean(name, type)
            is Int -> editor.putInt(name, type)
            is Float -> editor.putFloat(name, type)
            is String -> editor.putString(name, type)
            is Long -> editor.putLong(name, type)
        }

        editor.apply()
    }

    fun getBoolean(pKey: String, pDefValue: Boolean = true): Boolean =
        getPreferences().getBoolean(pKey, pDefValue)

    fun getString(pKey: String, pDefValue: String = ""): String =
        getPreferences().getString(pKey, pDefValue) ?: pDefValue

    fun getInt(pKey: String, pDefValue: Int = 0): Int = getPreferences().getInt(pKey, pDefValue)
    fun getLong(pKey: String, pDefValue: Long = 0L): Long =
        getPreferences().getLong(pKey, pDefValue)
}

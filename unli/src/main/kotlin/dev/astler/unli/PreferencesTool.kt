package dev.astler.unli

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager

open class PreferencesTool (context: Context?) {

    companion object {

        fun newInstance(context: Context) = PreferencesTool(context)

        const val darkAppTheme = "darkAppTheme"
        const val systemAppTheme = "systemAppTheme"

        const val appLocale = "appLocale"
        const val appLocaleDefault = "system"
        const val appLocaleMode = "appLocaleMode"
        const val appLocaleModeDefault = false

        const val textSize = "textSize"
        const val textSizeDefault = "18"

        const val useEnglishKey = "useEnglish"


        const val customBackgroundKey = "customBackground"
    }

    private var mPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPreferences(): SharedPreferences {
        return mPreferences
    }

    fun getTextSize(): Float {
        return (mPreferences.getString(textSize, textSizeDefault)?: textSizeDefault).toFloat()
    }

    fun useDarkTheme(): Boolean {
        return mPreferences.getBoolean(darkAppTheme, false)
    }

    fun systemTheme(): Boolean {
        return mPreferences.getBoolean(systemAppTheme, false)
    }

    fun switchTheme() {
        edit(darkAppTheme, !useDarkTheme())
    }

    private fun edit(name: String, type: Any) {
        val editor = mPreferences.edit()

        when(type) {
            is Boolean -> editor.putBoolean(name, type)
            is Int -> editor.putInt(name, type)
        }

        editor.apply()
    }

    fun addListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)  {
        mPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)  {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun chooseLanguageManually() = mPreferences.getBoolean(appLocaleMode, appLocaleModeDefault)
    fun getUserLanguage() = mPreferences.getString(appLocale, appLocaleDefault) ?: appLocaleDefault

    fun isFirstStartForVersion(versionCode: Int) = mPreferences.getBoolean("firstStartVersion$versionCode", true)

    fun setFirstStartForVersion(versionCode: Int) {
        edit("firstStartVersion$versionCode", false)
    }

    var useEnglish: Boolean
        get() = mPreferences.getBoolean(useEnglishKey, false)
        set(useEnglish) {
            edit(useEnglishKey, useEnglish)
        }

//    var backgroundColor: Int
//        get() = mPreferences.getInt(customBackgroundKey, ContextCompat.getColor(context, R.color.default_background_color))
//        set(backgroundColor) = edit(customBackgroundKey, backgroundColor)
}

val Context.appPrefs: PreferencesTool get() = PreferencesTool.newInstance(this)
package dev.astler.catlib

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.color.DynamicColors
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dev.astler.catlib.core.BuildConfig
import dev.astler.catlib.extensions.defaultNightMode
import dev.astler.catlib.localization.LocalizationManager
import dev.astler.catlib.preferences.PreferencesTool
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

val catJson = Json {
    allowStructuredMapKeys = true
}

open class PressFAplication : Application(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var preferences: PreferencesTool

    @Inject
    lateinit var localizationManager: LocalizationManager

    companion object {
        private lateinit var applicationInstance: PressFAplication

        @Synchronized
        fun getInstance() = applicationInstance
    }

    override fun onCreate() {
        super.onCreate()

        applicationInstance = this

        val savedLanguage = preferences.getString(PreferencesTool.appLocaleKey, LocalizationManager.SYSTEM_LANGUAGE)
        localizationManager.setAppLocale(savedLanguage)

        AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DynamicColors.applyToActivitiesIfAvailable(this)

        preferences.addListener(this)

        createNotificationChannels()
        plantTimber()

        FirebaseApp.initializeApp(this)
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }

    open fun createNotificationChannels() {}

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        when (key) {
            PreferencesTool.appThemeKey -> {
                AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
            }
            PreferencesTool.appLocaleKey -> {
                val newLocale = sp?.getString(key, LocalizationManager.SYSTEM_LANGUAGE)
                    ?: LocalizationManager.SYSTEM_LANGUAGE
                localizationManager.setAppLocale(newLocale)
            }
        }
    }
}
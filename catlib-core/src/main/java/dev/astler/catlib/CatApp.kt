package dev.astler.catlib

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import dev.astler.catlib.extensions.defaultNightMode
import dev.astler.catlib.preferences.PreferencesTool
import kotlinx.serialization.json.Json
import javax.inject.Inject

val catJson = Json {
    allowStructuredMapKeys = true
}

open class CatApp : Application(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    protected lateinit var preferences: PreferencesTool

    companion object {
        private lateinit var applicationInstance: CatApp

        @Synchronized
        fun getInstance() = applicationInstance
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DynamicColors.applyToActivitiesIfAvailable(this)

        preferences.addListener(this)

        applicationInstance = this

        createNotificationChannels()
    }

    open fun createNotificationChannels() {}

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.appThemeKey) {
            AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
        }
    }
}

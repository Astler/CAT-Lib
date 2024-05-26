package dev.astler.catlib

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dev.astler.catlib.core.BuildConfig
import dev.astler.catlib.extensions.defaultNightMode
import dev.astler.catlib.preferences.PreferencesTool
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

val catJson = Json {
    allowStructuredMapKeys = true
}

open class CatApp : Application(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var preferences: PreferencesTool

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
        plantTimber()

        FirebaseApp.initializeApp(this)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }

    open fun createNotificationChannels() {}

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.appThemeKey) {
            AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
        }
    }
}

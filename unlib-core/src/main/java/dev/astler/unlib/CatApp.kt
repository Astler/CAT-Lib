package dev.astler.unlib

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import dev.astler.unlib.config.AppConfig
import dev.astler.unlib.core.R
import dev.astler.unlib.utils.readFileFromRaw
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val gPreferencesTool: PreferencesTool by lazy {
    CatApp.prefs
}

val gAppConfig: AppConfig by lazy {
    CatApp.cAppConfig
}

val mJson = Json { allowStructuredMapKeys = true }

fun getDefaultNightMode() = when (CatApp.prefs.appTheme) {
    "light" -> AppCompatDelegate.MODE_NIGHT_NO
    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
    else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    } else {
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    }
}

open class CatApp : LocaleAwareApplication(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        lateinit var prefs: PreferencesTool
        lateinit var cAppConfig: AppConfig

        private lateinit var applicationInstance: CatApp

        @Synchronized
        fun getInstance() = applicationInstance
    }

    override fun onCreate() {
        super.onCreate()

        prefs = PreferencesTool(this)

        AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DynamicColors.applyToActivitiesIfAvailable(this)

        prefs.addListener(this)

        applicationInstance = this

        val nAppConfig = readFileFromRaw(R.raw.app_config)

        cAppConfig = if (nAppConfig.isNotEmpty()) {
            mJson.decodeFromString(nAppConfig)
        } else {
            AppConfig()
        }

        createNotificationChannels()
    }

    open fun createNotificationChannels() {}

    fun createNotificationChannel(
        pName: String = packageName,
        pDescription: String = "",
        pChannelId: String = "unli_default"
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(pChannelId, pName, importance).apply {
                description = pDescription
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.appThemeKey) {
            AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
        }
    }
}

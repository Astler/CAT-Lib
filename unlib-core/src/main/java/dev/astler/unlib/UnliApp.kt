package dev.astler.unlib

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import dev.astler.unlib.config.AppConfig
import dev.astler.unlib.core.R
import dev.astler.unlib.utils.readFileFromRaw
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val gPreferencesTool: PreferencesTool by lazy {
    UnliApp.prefs
}

val gAppConfig: AppConfig by lazy {
    UnliApp.cAppConfig
}

val mJson = Json { allowStructuredMapKeys = true }

/**
 * config version: 1
 * Astler; 19/03/2021
 */

fun getDefaultNightMode() = when (UnliApp.prefs.appTheme) {
    "light" -> AppCompatDelegate.MODE_NIGHT_NO
    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
    else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    } else {
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    }
}

open class UnliApp : MultiDexApplication() {

    companion object {
        lateinit var prefs: PreferencesTool
        lateinit var cAppConfig: AppConfig

        private lateinit var applicationInstance: UnliApp

        @Synchronized
        fun getInstance(): UnliApp {
            return applicationInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesTool(this)

        AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())

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

    fun initAppLanguage(context: Context) {
        AppSettings.loadLocale(
            context
        )
    }

    fun createNotificationChannel(pName: String = packageName, pDescription: String = "", pChannelId: String = "unli_default") {
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
}

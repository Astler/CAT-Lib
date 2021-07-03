package dev.astler.unlib

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import dev.astler.unlib.config.AppConfig
import dev.astler.unlib.core.R
import dev.astler.unlib.utils.readFileFromRaw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.* // ktlint-disable no-wildcard-imports

val gAppConfig: AppConfig by lazy {
    UnliApp.cAppConfig
}

val mJson = Json { allowStructuredMapKeys = true }

/**
 * config version: 1
 * Astler; 19/03/2021
 */

open class UnliApp : MultiDexApplication() {

    protected val mApplicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var mNoAdsDay = -1
    open var mAdsDisabled = true

    var mFontSize = cTextSizeDefault
    var mAppTheme = cSystemDefault
    var mAppLanguage = cSystemDefault
    var mAppStartCounter = 0

    val mDataStorePreferences: DataStore<Preferences> by preferencesDataStore(
        name = PreferencesKeys.PREFERENCE_NAME,
        produceMigrations = {
            listOf(
                SharedPreferencesMigration({
                    PreferenceManager.getDefaultSharedPreferences(it)
                })
            )
        }
    )

    companion object {
        lateinit var cAppConfig: AppConfig

        private lateinit var applicationInstance: UnliApp

        @Synchronized
        fun getInstance(): UnliApp {
            return applicationInstance
        }
    }

    override fun onCreate() {
        super.onCreate()

        applicationInstance = this

        val nAppConfig = readFileFromRaw(R.raw.app_config)

        cAppConfig = if (nAppConfig.isNotEmpty()) {
            mJson.decodeFromString(nAppConfig)
        } else {
            AppConfig()
        }

        mApplicationScope.launch {
            LocalStorage.textSizeWatcher { mFontSize = it }
            LocalStorage.appThemeWatcher { mAppTheme = it }
            LocalStorage.appLanguageWatcher { mAppLanguage = it }

            mAppStartCounter = LocalStorage.startupTimes()

            LocalStorage.setReviewRequestTime(GregorianCalendar().timeInMillis)

            LocalStorage.setStartTime(GregorianCalendar().timeInMillis)
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

package dev.astler.unlib.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dev.astler.unlib.core.BuildConfig
import dev.astler.unlib.core.R

class RemoteConfig {

    private val mFirebaseRemoteConfig: FirebaseRemoteConfig

    fun fetchData(onFetchComplete: OnFetchComplete?) {
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { onFetchComplete?.onComplete() }
    }

    fun getBoolean(pName: String): Boolean = mFirebaseRemoteConfig.getBoolean(pName)
    fun getLong(pName: String): Long = mFirebaseRemoteConfig.getLong(pName)
    fun getString(pName: String): String = mFirebaseRemoteConfig.getString(pName)

    val mAppVersion: Long
        get() = mFirebaseRemoteConfig.getLong(APP_VERSION)

    interface OnFetchComplete {
        fun onComplete()
    }

    companion object {
        private const val APP_VERSION = "app_version"

        private var instance: RemoteConfig? = null
        fun getInstance(): RemoteConfig {
            return instance ?: RemoteConfig()
        }
    }

    init {
        instance = this
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 5 else 60 * 30.toLong())
                .build()
        )

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
}

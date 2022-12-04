package dev.astler.catlib.remote_config

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dev.astler.catlib.core.BuildConfig
import dev.astler.catlib.core.R
import dev.astler.catlib.utils.infoLog
import dev.astler.catlib.utils.toast
import javax.inject.Inject

class RemoteConfigProvider @Inject constructor(context: Context) {

    private val _remoteConfig by lazy { Firebase.remoteConfig }

    fun getBoolean(key: String): Boolean = _remoteConfig.getBoolean(key)
    fun getLong(key: String): Long = _remoteConfig.getLong(key)
    fun getString(key: String): String = _remoteConfig.getString(key)

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds =
                if (BuildConfig.DEBUG) 5 else 60 * 30.toLong()
        }

        _remoteConfig.setConfigSettingsAsync(configSettings)
        _remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        if (context is AppCompatActivity) {
            _remoteConfig.fetchAndActivate().addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    infoLog("Fetch and activate succeeded")
                } else {
                    infoLog("Fetch failed")
                }
            }
        }
    }
}
package dev.astler.catlib.analytics

import android.annotation.SuppressLint
import android.content.Context
import com.devtodev.analytics.external.DTDLogLevel
import com.devtodev.analytics.external.analytics.DTDAnalytics
import com.devtodev.analytics.external.analytics.DTDAnalyticsConfiguration
import com.devtodev.analytics.external.analytics.DTDCustomEventParameters
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.infoLog
import javax.inject.Inject

open class CatAnalytics @Inject constructor(var context: Context, val config: AppConfig) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: CatAnalytics? = null

        fun truckCustomEvent(key: String, vararg params: Pair<String, Any>) {
            instance?.simpleEvent(key, *params)
        }
    }

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        if (instance != null) return

        if (config.d2dAppId.isEmpty()) {
            infoLog("Id is empty, not initialized", javaClass.simpleName)
            return
        }

        val analyticsConfiguration = DTDAnalyticsConfiguration()
        analyticsConfiguration.logLevel = DTDLogLevel.Error
        DTDAnalytics.initialize(config.d2dAppId, analyticsConfiguration, context)

        infoLog("Initialized", javaClass.simpleName)

        instance = this
    }

    protected fun simpleEvent(key: String, vararg params: Pair<String, Any>) {
        val parameters = DTDCustomEventParameters()

        params.forEach { pair ->
            when (pair.second) {
                is String -> parameters.add(pair.first, pair.second as String)
                is Long -> parameters.add(pair.first, pair.second as Long)
                is Double -> parameters.add(pair.first, pair.second as Double)
                else -> parameters.add(pair.first, pair.second.toString())
            }
        }

        infoLog("Event $key $parameters", javaClass.simpleName)

        DTDAnalytics.customEvent(
            eventName = key, customEventParameters = parameters
        )
    }
}
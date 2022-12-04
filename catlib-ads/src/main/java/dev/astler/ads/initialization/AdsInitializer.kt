package dev.astler.ads.initialization

import android.content.Context
import androidx.startup.Initializer
import dev.astler.ads.hilt.InitializerEntryPoint
import javax.inject.Inject

class AdsInitializer : Initializer<AdsCore> {

    @Inject
    lateinit var analyticsService: AdsCore

    override fun create(context: Context): AdsCore {
        InitializerEntryPoint.resolve(context).inject(this)
        return analyticsService
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf()
    }
}
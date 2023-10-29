package dev.astler.ads.initialization

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ads.AppOpenManager
import dev.astler.ads.utils.NativeAdsLoader
import dev.astler.ads.utils.isGoogleTestDevice
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.infoLog
import javax.inject.Inject

class AdsCore @Inject constructor(
    context: Context, preferences: PreferencesTool, config: AppConfig
) {
    private var _appOpenManager: AppOpenManager? = null
    private var _nativeAdsLoader: NativeAdsLoader? = null

    init {
        if (context.isGoogleTestDevice()) {
            infoLog("started on google device")
        } else {
            infoLog("started on default device")
            MobileAds.initialize(context)

            if (context is Application)
                _appOpenManager = AppOpenManager(context, preferences, config)

            if (config.nativeAdId.isNotEmpty()) {
                _nativeAdsLoader = NativeAdsLoader(config)
            }
        }
    }
}
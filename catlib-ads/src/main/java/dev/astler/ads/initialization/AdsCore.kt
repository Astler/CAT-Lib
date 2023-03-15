package dev.astler.ads.initialization

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ads.AppOpenManager
import dev.astler.ads.utils.NativeAdsLoader
import dev.astler.catlib.config.AppConfig
import javax.inject.Inject

class AdsCore @Inject constructor(context: Context, preferences: PreferencesTool, config: AppConfig) {
    private var _appOpenManager: AppOpenManager? = null
    private var _nativeAdsLoader: NativeAdsLoader? = null

    init {
        MobileAds.initialize(context)

        if (context is Application)
            _appOpenManager = AppOpenManager(context, preferences, config)

        if (config.mNativeAdId.isNotEmpty()) {
            _nativeAdsLoader = NativeAdsLoader(config)
        }
    }
}
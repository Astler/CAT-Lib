package dev.astler.ads.initialization

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ads.AppOpenManager
import javax.inject.Inject

class AdsCore @Inject constructor(context: Context, preferences: PreferencesTool) {
    private var _appOpenManager: AppOpenManager? = null

    init {
        MobileAds.initialize(context)

        if (context is Application)
            _appOpenManager = AppOpenManager(context, preferences)
    }
}
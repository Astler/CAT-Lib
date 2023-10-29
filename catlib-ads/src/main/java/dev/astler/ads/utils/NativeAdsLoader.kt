package dev.astler.ads.utils

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import dev.astler.catlib.config.AppConfig
import javax.inject.Inject

class NativeAdsLoader @Inject constructor(
    val appConfig: AppConfig
) {
    var adLoader: AdLoader? = null
    private var nativeAds: MutableList<NativeAd> = ArrayList()

    var nativeAdsTmp: MutableList<NativeAd> = ArrayList()

    init {
        instance = this
    }

    fun loadAds(context: Context, adRequest: AdRequest?) {
        val build: AdLoader =
            AdLoader.Builder(context, appConfig.nativeAdId).forNativeAd { unifiedNativeAd ->
                nativeAdsTmp.add(unifiedNativeAd)
                if (!adLoader!!.isLoading) {
                    nativeAds.clear()
                    nativeAds.addAll(nativeAdsTmp)
                    nativeAdsTmp.clear()
                }
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)

                    if (!adLoader!!.isLoading) {
                        nativeAdsTmp.clear()
                    }
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().setAdChoicesPlacement(1).build())
                .build()

        adLoader = build

        adRequest?.apply {
            build.loadAds(adRequest, NUMBER_OF_ADS)
        }
    }

    fun getNativeAd(i: Int): NativeAd? {
        if (nativeAds.isEmpty())
            return null

        if (i >= nativeAds.size) {
            return nativeAds[nativeAds.size - 1]
        }

        return nativeAds[i]
    }

    fun getRandomNativeAd(): NativeAd? {
        if (nativeAds.isEmpty())
            return null

        return nativeAds.random()
    }

    companion object {
        const val NUMBER_OF_ADS = 5
        var instance: NativeAdsLoader? = null
            private set
    }
}

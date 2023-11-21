package dev.astler.ads.data

import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.remote_config.RemoteConfigProvider

const val startAdDelayKey = "start_ad_delay_"
const val showStartAdKey = "start_ad_show_"

const val interstitialAdEnabledKey = "interstitial_ad_enabled_"

const val bannerAdEnabledKey = "banner_ad_enabled_"
const val rewardAdEnabledKey = "reward_ad_enabled_"

const val startAdOtherAdDelayKey = "start_ad_other_ad_delay_"

data class RemoteConfigData(
    private val remoteConfig: RemoteConfigProvider?,
    private val configPackageName: String?
) {
    constructor() : this(null, null)

    val isEmpty get() = remoteConfig == null

    /**
     * Delay in second which will be min use time to show and in one session!
     */
    var startAdDelay: Long = 60

    /**
     * Required delay between two ads (only open app or interstitial + rewarded ads)
     */
    var startAdOtherAdDelay: Long = 20

    /**
     * Enabled options to control ads
     */
    var isStartAdsEnabled: Boolean = true
    var interstitialAdEnabled: Boolean = true
    var bannerAdEnabled: Boolean = false
    var rewardAdEnabled: Boolean = true

    init {
        infoLog("remoteConfig = $remoteConfig")
        if (remoteConfig != null) {
            infoLog(startAdDelayKey + configPackageName)
            startAdDelay = remoteConfig.getLong(startAdDelayKey + configPackageName)
            startAdOtherAdDelay =  remoteConfig.getLong(startAdOtherAdDelayKey + configPackageName)

            isStartAdsEnabled = remoteConfig.getBoolean(showStartAdKey + configPackageName)
            interstitialAdEnabled =
                remoteConfig.getBoolean(interstitialAdEnabledKey + configPackageName)
            bannerAdEnabled = remoteConfig.getBoolean(bannerAdEnabledKey + configPackageName)
            rewardAdEnabled = remoteConfig.getBoolean(rewardAdEnabledKey + configPackageName)
        }
    }
}
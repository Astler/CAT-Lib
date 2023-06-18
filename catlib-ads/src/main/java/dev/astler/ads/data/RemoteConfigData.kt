package dev.astler.ads.data

//region remote config

const val startAdDelayKey = "start_ad_delay_"
const val showStartAdKey = "start_ad_show_"
const val startAdOtherAdDelayKey = "start_ad_other_ad_delay_"


const val resumeAdDelayKey = "resume_ad_delay_"
const val lastAdDelayKey = "last_ad_delay_"
const val adChanceKey = "ad_chance_"
const val interstitialAdEnabledKey = "interstitial_ad_enabled_"
const val openAdEnabledKey = "open_ad_enabled_"
const val bannerAdEnabledKey = "banner_ad_enabled_"
const val rewardAdEnabledKey = "reward_ad_enabled_"

data class RemoteConfigData(
    val startAdDelay: Long = 5 * 60,
    val resumeAdDelay: Long = 15,
    val lastAdDelay: Long = 60,
    val adChance: Long = 0,
    val interstitialAdEnabled: Boolean = true,
    val openAdEnabled: Boolean = true,
    val bannerAdEnabled: Boolean = false,
    val rewardAdEnabled: Boolean = true,
    val isStartAdsEnabled: Boolean = true,
    val startAdOtherAdDelay: Long = 20,
)

//endregion
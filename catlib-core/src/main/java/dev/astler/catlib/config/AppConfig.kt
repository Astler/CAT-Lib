package dev.astler.catlib.config

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val mProPackageName: String = "",
    val mMainActivityPackage: String = "",

    val mRewardedAdId: String = "",
    val mInterstitialAdId: String = "",
    val mNativeAdId: String = "",
    val mStartAdId: String = "",

    val mNeedAgeCheck: Boolean = false,
    val mTestDevices: List<String> = listOf(),
    val mBillingItems: List<String> = listOf(),
)

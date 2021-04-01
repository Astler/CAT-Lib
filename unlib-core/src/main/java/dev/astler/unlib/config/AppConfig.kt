package dev.astler.unlib.config

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val mMainActivityPackage: String = "",
    val mInterstitialAdId: String = "",
    val mRewardedAdId: String = "",
    val mProPackageName: String = "",
    val mLastTestDevice: String = "",
    val mNeedAgeCheck: Boolean = false,
    val mBillingItems: List<String> = listOf(),
)
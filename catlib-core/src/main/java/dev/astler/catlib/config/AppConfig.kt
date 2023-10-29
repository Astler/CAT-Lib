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

    val d2dAppId: String = "",
) {
    override fun toString(): String {
        return """
            Pro Version Package: ${mProPackageName.ifEmpty { "No Pro Version" }}
            Main Activity Package: ${mMainActivityPackage.ifEmpty { "---" }}
            Rewarded Id: ${mRewardedAdId.ifEmpty { "---" }}
            Interstitial Id: ${mInterstitialAdId.ifEmpty { "---" }}
            Native Id: ${mNativeAdId.ifEmpty { "---" }}
            Start Id: ${mStartAdId.ifEmpty { "---" }}
            Age Check Requirement: $mNeedAgeCheck
            Test Devices: $mTestDevices
            Billing Item: $mBillingItems
            D2D App Id: $d2dAppId
        """.trimIndent()
    }
}

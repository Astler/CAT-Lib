package dev.astler.catlib.config

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val proVersionPackageId: String = "",

    val interstitialAdId: String = "",
    val rewardedAdId: String = "",
    val nativeAdId: String = "",
    val startAdId: String = "",

    val ageRestricted: Boolean = false,
    val testDevices: List<String> = listOf(),
    val billingIds: List<String> = listOf(),

    val d2dAppId: String = "",
) {
    override fun toString(): String {
        return """
            Pro Version Package: ${proVersionPackageId.ifEmpty { "No Pro Version" }}
            Rewarded Id: ${rewardedAdId.ifEmpty { "---" }}
            Interstitial Id: ${interstitialAdId.ifEmpty { "---" }}
            Native Id: ${nativeAdId.ifEmpty { "---" }}
            Start Id: ${startAdId.ifEmpty { "---" }}
            Age Check Requirement: $ageRestricted
            Test Devices: $testDevices
            Billing Item: $billingIds
            D2D App Id: $d2dAppId
        """.trimIndent()
    }
}

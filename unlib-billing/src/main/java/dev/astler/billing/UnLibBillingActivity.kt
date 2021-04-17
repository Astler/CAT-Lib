package dev.astler.billing

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import com.android.billingclient.api.*
import dev.astler.unlib.cBillingNoAdsName
import dev.astler.unlib.cNoAdsName
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.utils.infoLog

abstract class UnLibBillingActivity : BaseUnLiActivity(), PerformBillingListener {

    private val mPurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, pPurchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && pPurchases != null) {
                pPurchases.forEach { pPurchase ->
                    updatePurchases(pPurchase)
                }
            }
        }
    //TODO ACKNOWLEDGE CONFIG!
    open fun updatePurchases(pPurchase: Purchase) {
        if (pPurchase.sku == cBillingNoAdsName) {
            gPreferencesTool.edit(cNoAdsName, true)
        }

        if (pPurchase.sku == cBillingNoAdsName) {
            if (!pPurchase.isAcknowledged) {
                infoLog("BILLING: Acknowledge pur")

                val acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(pPurchase.purchaseToken).build()

                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val billingResponseCode = billingResult.responseCode
                    val billingDebugMessage = billingResult.debugMessage

                    infoLog("BILLING: response code: $billingResponseCode")
                    infoLog("BILLING: debugMessage : $billingDebugMessage")
                }
            }
        }
    }

    val mBillingClient: BillingClient by lazy {
        BillingClient.newBuilder(this)
            .setListener(mPurchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    protected val mBillingViewModel by viewModels<BillingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBillingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    infoLog("BILLING: loaded data")

                    mBillingViewModel.queryItemsDetails(mBillingClient)
                }

                mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList?.forEach { pPurchase ->
                    infoLog("BILLING: got item = ${pPurchase.sku}")

                    infoLog("${pPurchase.purchaseState == Purchase.PurchaseState.PURCHASED}")

                    if (pPurchase.sku == cBillingNoAdsName)
                        gPreferencesTool.edit(
                            cNoAdsName,
                            pPurchase.purchaseState == Purchase.PurchaseState.PURCHASED
                        )

                    queryPurchases(pPurchase)
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    open fun queryPurchases(pPurchase: Purchase) {
        if (pPurchase.sku == cBillingNoAdsName) {
            if (!pPurchase.isAcknowledged) {
                infoLog("BILLING: Acknowledge pur")

                val acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(pPurchase.purchaseToken).build()

                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val billingResponseCode = billingResult.responseCode
                    val billingDebugMessage = billingResult.debugMessage

                    infoLog("BILLING: response code: $billingResponseCode")
                    infoLog("BILLING: debugMessage : $billingDebugMessage")
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        if (key == cNoAdsName) recreate()
    }

    override fun buyItem(pItemName: String) {
        mBillingViewModel.buySomething(mBillingClient, this, pItemName)
    }
}
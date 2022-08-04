package dev.astler.billing

import android.app.Activity
import com.android.billingclient.api.*
import dev.astler.unlib.cBillingNoAdsName
import dev.astler.unlib.cNoAdsName
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.utils.infoLog

abstract class CatBillingAssistant(
    private val activity: Activity,
    private val billingViewModel: BillingViewModel
) : PerformBillingListener {

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, pPurchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && pPurchases != null) {
                pPurchases.forEach { pPurchase ->
                    queryPurchases(pPurchase)
                }
            }
        }

    val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(activity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    init {
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    billingViewModel.queryItemsDetails(billingClient)
                }
            }
            override fun onBillingServiceDisconnected() {
                startConnection()
            }
        })
    }

    open fun queryPurchases(pPurchase: Purchase) {
        infoLog(pPurchase.orderId)
        infoLog(pPurchase.originalJson)

        pPurchase.products.forEach {
            if (it == cBillingNoAdsName) {
                gPreferencesTool.edit(
                    cNoAdsName,
                    pPurchase.purchaseState == Purchase.PurchaseState.PURCHASED
                )

                if (!pPurchase.isAcknowledged) {
                    infoLog("BILLING: Acknowledge pur")

                    val acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(pPurchase.purchaseToken).build()

                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        val billingResponseCode = billingResult.responseCode
                        val billingDebugMessage = billingResult.debugMessage

                        infoLog("BILLING: response code: $billingResponseCode")
                        infoLog("BILLING: debugMessage : $billingDebugMessage")
                    }
                }
            }
        }
    }

    override fun buyItem(pItemName: String) =
        billingViewModel.buySomething(billingClient, activity, pItemName)
}

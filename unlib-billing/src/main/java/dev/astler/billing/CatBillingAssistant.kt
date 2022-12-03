package dev.astler.billing

import android.app.Activity
import com.android.billingclient.api.*
import dev.astler.catlib.cBillingNoAdsName
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.utils.infoLog

interface IQueryPurchases {
    fun query(client: BillingClient, productId: String, purchase: Purchase)
    fun restorePurchases(billingResult: BillingResult, purchase: Purchase) {

    }
}

class CatBillingAssistant(
    private val queryActivity: Activity,
    private val billingViewModel: BillingViewModel
) : PerformBillingListener {

    private var activityQueries: IQueryPurchases? = null

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, pPurchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && pPurchases != null) {
                pPurchases.forEach { pPurchase ->
                    queryPurchases(pPurchase)
                }
            }
        }

    val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(queryActivity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    init {
        if (queryActivity is IQueryPurchases) {
            activityQueries = queryActivity
        }

        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingViewModel.queryItemsDetails(billingClient) { billingResult, purchase ->  
                        activityQueries?.restorePurchases(billingResult, purchase)
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                startConnection()
            }
        })
    }

    private fun queryPurchases(purchase: Purchase) {
        infoLog(purchase.orderId)
        infoLog(purchase.originalJson)

        purchase.products.forEach {
            if (it == cBillingNoAdsName) {
                gPreferencesTool.adsDisabled = purchase.purchaseState == Purchase.PurchaseState.PURCHASED

                if (!purchase.isAcknowledged) {
                    infoLog("BILLING: Acknowledge pur")

                    val acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken).build()

                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        val billingResponseCode = billingResult.responseCode
                        val billingDebugMessage = billingResult.debugMessage

                        infoLog("BILLING: response code: $billingResponseCode")
                        infoLog("BILLING: debugMessage : $billingDebugMessage")
                    }
                }
            }

            activityQueries?.query(billingClient, it, purchase)
        }
    }

    override fun buyItem(pItemName: String) =
        billingViewModel.buySomething(billingClient, queryActivity, pItemName)
}

package dev.astler.billing

import android.os.Handler
import android.os.Looper
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import dev.astler.billing.data.BillingViewModel
import dev.astler.billing.interfaces.IQueryPurchases
import dev.astler.billing.interfaces.PerformBillingListener
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.catlib.constants.cBillingNoAdsName
import dev.astler.catlib.helpers.infoLog

class CatBillingAssistant(
    private val queryActivity: CatActivity<*>,
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

    private var reconnectAttempts = 0

    init {
        if (queryActivity is IQueryPurchases) {
            activityQueries = queryActivity
        }

        startConnection()
    }


    private fun startConnection() {
        if (reconnectAttempts > 3) {
            // Max attempts reached. Do something to handle this situation
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    reconnectAttempts = 0 // reset count on successful connection
                    billingViewModel.queryItemsDetails(billingClient) { billingResult, purchase ->
                        activityQueries?.restorePurchases(billingResult, purchase)
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                reconnectAttempts++
                Handler(Looper.getMainLooper()).postDelayed(
                    { startConnection() },
                    3000
                ) // Wait 3 seconds before attempting to reconnect
            }
        })
    }

    private fun queryPurchases(purchase: Purchase) {
        infoLog(purchase.orderId ?: "null")
        infoLog(purchase.originalJson)

        purchase.products.forEach {
            if (it == cBillingNoAdsName) {
                queryActivity.preferences.adsDisabled = purchase.purchaseState == Purchase.PurchaseState.PURCHASED

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

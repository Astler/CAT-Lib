package dev.astler.billing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.utils.infoLog
import kotlinx.coroutines.launch

abstract class UnLibBillingActivity : BaseUnLiActivity(), PerformBillingListener {

    private val mPurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, pPurchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && pPurchases != null) {
                pPurchases.forEach { pPurchase ->
                    queryPurchases(pPurchase)
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

                mBillingClient.queryPurchasesAsync(
                    BillingClient.SkuType.INAPP
                ) { _, pList ->

                    pList.forEach { pPurchase ->

                        pPurchase.skus.forEach { pPurchaseSku ->
                            infoLog("BILLING: got item = $pPurchaseSku")

                            if (pPurchaseSku == cBillingNoAdsName) {
                                lifecycleScope.launch {
                                    LocalStorage.storeValue(
                                        PreferencesKeys.ADS_DISABLED_KEY,
                                        pPurchase.purchaseState == Purchase.PurchaseState.PURCHASED
                                    )
                                }
                            }
                        }

                        infoLog("${pPurchase.purchaseState == Purchase.PurchaseState.PURCHASED}")

                        queryPurchases(pPurchase)
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    open fun queryPurchases(pPurchase: Purchase) {

        infoLog(pPurchase.orderId)
        infoLog(pPurchase.originalJson)

        pPurchase.skus.forEach {
            if (it == cBillingNoAdsName) {
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
    }

    override fun buyItem(pItemName: String) {
        mBillingViewModel.buySomething(mBillingClient, this, pItemName)
    }
}

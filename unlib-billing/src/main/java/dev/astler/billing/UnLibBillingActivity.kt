package dev.astler.billing

import android.os.Bundle
import androidx.activity.viewModels
import com.android.billingclient.api.*
import dev.astler.unlib.cBillingNoAdsName
import dev.astler.unlib.cNoAdsName
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.utils.infoLog

class UnLibBillingActivity : BaseUnLiActivity() {

    private val mPurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    if (purchase.sku == cBillingNoAdsName) {
                        gPreferencesTool.edit(cNoAdsName, true)
                    }
                }
            }
        }

    private val mBillingClient: BillingClient by lazy {
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

                mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList?.forEach {
                    infoLog("BILLING: got item = ${it.sku}")

                    if (it.sku == cBillingNoAdsName)
                        gPreferencesTool.edit(
                            cNoAdsName,
                            it.purchaseState == Purchase.PurchaseState.PURCHASED
                        )
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }
}
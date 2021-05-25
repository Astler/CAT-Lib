package dev.astler.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.billingclient.api.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.cBillingNoAdsName
import dev.astler.unlib.gAppConfig
import dev.astler.unlib.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BillingViewModel(pApp: Application) : AndroidViewModel(pApp) {

    private val mItemsList = ArrayList<SkuDetails>()

    fun queryItemsDetails(pBillingClient: BillingClient) {
        val skuList = ArrayList(gAppConfig.mBillingItems)
        skuList.add(cBillingNoAdsName)

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        GlobalScope.launch(Dispatchers.IO) {
            pBillingClient.querySkuDetailsAsync(params.build()) { _, skuDetailsList ->
                skuDetailsList?.forEach {
                    infoLog("loaded = ${it.sku}")
                    mItemsList.add(it)
                }
            }
        }
    }

    fun buySomething(pBillingClient: BillingClient, pActivity: Activity, pSku: String = "") {
        infoLog("BILLING: try to buy = $pSku")

        var nSkuDetails: SkuDetails? = null

        mItemsList.forEach {
            if (it.sku == pSku)
                nSkuDetails = it
        }

        nSkuDetails?.let {
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(it)
                .build()
            val responseCode = pBillingClient.launchBillingFlow(pActivity, flowParams).responseCode
            infoLog("responseCode = $responseCode")
        }
    }
}

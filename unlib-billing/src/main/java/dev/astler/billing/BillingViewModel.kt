package dev.astler.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import dev.astler.unlib.cBillingNoAdsName
import dev.astler.unlib.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BillingViewModel(pApp: Application): AndroidViewModel(pApp) {

    private val mItemsList = ArrayList<SkuDetails>()

    fun queryItemsDetails(pBillingClient: BillingClient) {
        val skuList = listOf(cBillingNoAdsName)

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        GlobalScope.launch(Dispatchers.IO) {
            pBillingClient.querySkuDetailsAsync(params.build()) { _, skuDetailsList ->
                skuDetailsList?.forEach {
                    mItemsList.add(it)
                }
            }
        }
    }

    fun buySomething(pBillingClient: BillingClient, pActivity: Activity, pSku: String = "") {
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
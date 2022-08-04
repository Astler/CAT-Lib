package dev.astler.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.* // ktlint-disable no-wildcard-imports
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.google.common.collect.ImmutableList
import dev.astler.unlib.cBillingNoAdsName
import dev.astler.unlib.gAppConfig
import dev.astler.unlib.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BillingViewModel(pApp: Application) : AndroidViewModel(pApp) {

    private val mItemsList = ArrayList<ProductDetails>()

    fun queryItemsDetails(billingClient: BillingClient) {
        val productList = ArrayList<Product>()

        gAppConfig.mBillingItems.forEach {
            productList.add(
                Product.newBuilder().setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP).build()
            )
        }

        //TODO Move to config
        productList.add(
            Product.newBuilder().setProductId(cBillingNoAdsName)
                .setProductType(BillingClient.ProductType.INAPP).build()
        )

        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        viewModelScope.launch(Dispatchers.IO) {
            billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
                productDetailsList.forEach {
                    infoLog("loaded = ${it.productId}")
                    mItemsList.add(it)
                }
            }
        }
    }

    fun buySomething(billingClient: BillingClient, activity: Activity, productId: String = "") {
        var productDetails: ProductDetails? = null

        mItemsList.forEach {
            if (it.productId == productId) productDetails = it
        }

        if (productDetails == null) return

        val nonNullDetails = productDetails as ProductDetails

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(nonNullDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
    }
}

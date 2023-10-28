package dev.astler.billing.data

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.QueryPurchasesParams
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.astler.catlib.constants.cBillingNoAdsName
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.helpers.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    app: Application,
    private val appConfig: AppConfig,
    private val preferences: PreferencesTool
)  : AndroidViewModel(app) {

    private val productsList = ArrayList<ProductDetails>()

    fun queryItemsDetails(
        billingClient: BillingClient,
        onQueryPurchase: (BillingResult, Purchase) -> Unit
    ) {
        val productList = ArrayList<Product>()

        appConfig.mBillingItems.forEach {
            productList.add(
                Product.newBuilder().setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP).build()
            )
        }

        productList.add(
            Product.newBuilder().setProductId(cBillingNoAdsName)
                .setProductType(BillingClient.ProductType.INAPP).build()
        )

        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        viewModelScope.launch(Dispatchers.IO) {
            billingClient.queryProductDetailsAsync(queryProductDetailsParams) { _, productDetailsList ->
                productDetailsList.forEach {
                    infoLog("loaded = ${it.productId}")
                    productsList.add(it)
                }
            }

            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            ) { billingResult, purchaseList ->
                purchaseList.forEach {
                    if (it.products.contains(cBillingNoAdsName)) {
                        preferences.adsDisabled = it.purchaseState == Purchase.PurchaseState.PURCHASED
                    } else {
                        onQueryPurchase(billingResult, it)
                    }

                }
            }
        }
    }

    fun buySomething(billingClient: BillingClient, activity: Activity, productId: String = "") {
        var productDetails: ProductDetails? = null

        productsList.forEach {
            if (it.productId == productId) productDetails = it
        }

        if (productDetails == null) return

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails!!)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

        infoLog("billingResult = ${billingResult.responseCode}")
    }
}

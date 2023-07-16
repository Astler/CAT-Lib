package dev.astler.billing.interfaces

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase

interface IQueryPurchases {
    fun query(client: BillingClient, productId: String, purchase: Purchase)
    fun restorePurchases(billingResult: BillingResult, purchase: Purchase) {

    }
}
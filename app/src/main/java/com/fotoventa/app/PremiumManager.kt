package com.fotoventa.app

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams

/**
 * Premium $299 MXN, compra única de por vida (SPEC-04).
 * Es caro a propósito: casi nadie lo compra, casi todos ven ads.
 */
class PremiumManager(
    context: Context,
    private val onPurchaseConfirmed: () -> Unit
) : PurchasesUpdatedListener {

    companion object {
        const val PRODUCT_ID = "fotoventa_premium_299"
        private const val PREFS = "fotoventa_premium"
        private const val KEY_PREMIUM = "premium"
    }

    private val appContext = context.applicationContext

    private val billingClient: BillingClient = BillingClient.newBuilder(appContext)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    fun isPremium(): Boolean =
        appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_PREMIUM, false)

    private fun setPremium(value: Boolean) {
        appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_PREMIUM, value).apply()
    }

    fun startConnection(onReady: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    restorePurchases()
                    onReady()
                }
            }

            override fun onBillingServiceDisconnected() = Unit
        })
    }

    fun launchPurchase(activity: Activity) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        billingClient.queryProductDetailsAsync(params) { _, detailsList ->
            val product = detailsList.firstOrNull() ?: return@queryProductDetailsAsync
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(product)
                            .build()
                    )
                ).build()
            billingClient.launchBillingFlow(activity, flowParams)
        }
    }

    /** Restaura Premium al reinstalar con la misma cuenta de Google. */
    fun restorePurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, purchases ->
            val owned = purchases.any {
                it.products.contains(PRODUCT_ID) &&
                    it.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            setPremium(owned)
            if (owned) onPurchaseConfirmed()
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: List<Purchase>?) {
        if (result.responseCode != BillingClient.BillingResponseCode.OK) return
        val owned = purchases?.any {
            it.products.contains(PRODUCT_ID) &&
                it.purchaseState == Purchase.PurchaseState.PURCHASED
        } == true
        if (owned) {
            setPremium(true)
            onPurchaseConfirmed()
        }
    }
}

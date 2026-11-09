package com.fotoventa.app

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * AdMob (SPEC-03). Solo IDs de PRUEBA de Google por defecto.
 * Los IDs reales van en local.properties, nunca en el repo.
 */
class AdManager(private val context: Context) {

    companion object {
        const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/1033173712"
        const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/4411468910"
    }

    private var interstitial: InterstitialAd? = null
    private var interstitialReady = false

    fun init() {
        MobileAds.initialize(context)
        loadInterstitial()
    }

    fun loadBanner(adView: AdView) {
        adView.adUnitId = TEST_BANNER_ID
        adView.loadAd(AdRequest.Builder().build())
    }

    fun loadInterstitial() {
        interstitialReady = false
        InterstitialAd.load(
            context,
            TEST_INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitial = ad
                    interstitialReady = true
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitial = null
                    interstitialReady = false
                }
            }
        )
    }

    /**
     * Muestra el interstitial al llegar al límite. Llama a onDone en ambos
     * casos (visto o no cargado) para que la UI pueda avisar y reintentar;
     * el extra solo se concede cuando el ad se mostró y se cerró.
     */
    fun showInterstitial(activity: Activity, onShown: () -> Unit, onNotAvailable: () -> Unit) {
        val ad = interstitial
        if (ad == null || !interstitialReady) {
            loadInterstitial()
            onNotAvailable()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitial = null
                interstitialReady = false
                loadInterstitial()
                onShown()
            }

            override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                interstitial = null
                interstitialReady = false
                loadInterstitial()
                onNotAvailable()
            }
        }
        ad.show(activity)
    }
}

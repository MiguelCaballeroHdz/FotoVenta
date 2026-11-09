package com.fotoventa.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fotoventa.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: FotoVentaViewModel
    lateinit var limitManager: LimitManager
    lateinit var premiumManager: PremiumManager
    lateinit var adManager: AdManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FotoVentaViewModel::class.java]
        limitManager = LimitManager(this)
        adManager = AdManager(this).also { it.init() }
        premiumManager = PremiumManager(this) { refreshAdsVisibility() }
        premiumManager.startConnection { refreshAdsVisibility() }

        refreshAdsVisibility()
        if (savedInstanceState == null) {
            showFragment(CameraFragment.newInstance(CameraFragment.STEP_CAR), addToBackStack = false)
        }
    }

    private fun refreshAdsVisibility() {
        if (premiumManager.isPremium()) {
            binding.adBanner.visibility = View.GONE
        } else {
            binding.adBanner.visibility = View.VISIBLE
            adManager.loadBanner(binding.adBanner)
        }
    }

    fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val tx = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
        if (addToBackStack) tx.addToBackStack(null)
        tx.commit()
    }

    fun goToSheetPhoto() = showFragment(CameraFragment.newInstance(CameraFragment.STEP_SHEET))
    fun goToSpecsPhoto() = showFragment(CameraFragment.newInstance(CameraFragment.STEP_SPECS))
    fun goToTemplate() = showFragment(TemplateFragment())
    fun goToOcr(photoUri: android.net.Uri, target: String) =
        showFragment(OCRFragment.newInstance(photoUri, target))

    /**
     * Generar (SPEC-01 + SPEC-03): si no hay límite, genera directo.
     * Si se llegó al límite en gratis, muestra el interstitial y concede 1 extra.
     */
    fun attemptGenerate() {
        if (!viewModel.hasMinimumToGenerate()) {
            Toast.makeText(this, "Agrega al menos precio o descripción", Toast.LENGTH_SHORT).show()
            return
        }
        val premium = premiumManager.isPremium()
        if (limitManager.canGenerate(premium)) {
            limitManager.recordGeneration(premium)
            showFragment(ResultFragment())
        } else {
            adManager.showInterstitial(
                activity = this,
                onShown = {
                    limitManager.grantExtra()
                    limitManager.recordGeneration(premium)
                    showFragment(ResultFragment())
                },
                onNotAvailable = {
                    Toast.makeText(
                        this,
                        "No se pudo cargar el anuncio, inténtalo de nuevo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    fun buyPremium() = premiumManager.launchPurchase(this)
}

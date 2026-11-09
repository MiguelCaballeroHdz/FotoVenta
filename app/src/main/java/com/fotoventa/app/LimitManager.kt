package com.fotoventa.app

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Límite gratis: 3 anuncios generados por día, contador local (SPEC-03).
 * Reinicia a medianoche, hora local del dispositivo.
 */
class LimitManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fotoventa_limites", Context.MODE_PRIVATE)

    companion object {
        const val FREE_LIMIT_PER_DAY = 3
        private const val KEY_DATE = "fecha"
        private const val KEY_COUNT = "contador"
        private const val KEY_EXTRA = "extra_desbloqueado"
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun resetIfNewDay() {
        if (prefs.getString(KEY_DATE, "") != today()) {
            prefs.edit()
                .putString(KEY_DATE, today())
                .putInt(KEY_COUNT, 0)
                .putBoolean(KEY_EXTRA, false)
                .apply()
        }
    }

    fun canGenerate(isPremium: Boolean): Boolean {
        if (isPremium) return true
        resetIfNewDay()
        val count = prefs.getInt(KEY_COUNT, 0)
        val extra = prefs.getBoolean(KEY_EXTRA, false)
        return count < FREE_LIMIT_PER_DAY || extra
    }

    fun recordGeneration(isPremium: Boolean) {
        if (isPremium) return
        resetIfNewDay()
        val count = prefs.getInt(KEY_COUNT, 0)
        if (prefs.getBoolean(KEY_EXTRA, false)) {
            // El extra cubre esta generación; no suma al contador.
            prefs.edit().putBoolean(KEY_EXTRA, false).apply()
        } else {
            prefs.edit().putInt(KEY_COUNT, count + 1).apply()
        }
    }

    /** Desbloquea 1 anuncio extra tras ver el interstitial (SPEC-03). */
    fun grantExtra() {
        resetIfNewDay()
        prefs.edit().putBoolean(KEY_EXTRA, true).apply()
    }
}

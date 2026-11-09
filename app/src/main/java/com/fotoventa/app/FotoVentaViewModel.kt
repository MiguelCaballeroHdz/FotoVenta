package com.fotoventa.app

import android.net.Uri
import androidx.lifecycle.ViewModel

/** Estado compartido del flujo. Nada se guarda en la nube. */
class FotoVentaViewModel : ViewModel {
    var carPhotoUri: Uri? = null
    var sheetPhotoUri: Uri? = null
    var specsPhotoUri: Uri? = null

    var marcaModelo: String = ""
    var precio: String = ""
    var km: String = ""
    var anio: String = ""
    var ubicacion: String = ""
    var whatsapp: String = ""
    var descripcion: String = ""

    /** Texto del anuncio, formato exacto de SPEC-05. Campos vacíos no aparecen. */
    fun buildAdText(isPremium: Boolean): String {
        val lines = mutableListOf<String>()
        if (marcaModelo.isNotBlank()) lines += "🚗 $marcaModelo"
        if (precio.isNotBlank()) lines += "💰 Precio: $$precio"
        if (ubicacion.isNotBlank()) lines += "📍 Ubicación: $ubicacion"
        if (km.isNotBlank()) lines += "🛣️ Kilometraje: $km km"
        if (whatsapp.isNotBlank()) lines += "📱 WhatsApp: $whatsapp"
        if (descripcion.isNotBlank()) {
            lines += ""
            lines += descripcion.trim()
        }
        if (!isPremium) {
            lines += ""
            lines += "Hecho con FotoVenta"
        }
        return lines.joinToString("\n")
    }

    fun hasMinimumToGenerate(): Boolean =
        precio.isNotBlank() || descripcion.isNotBlank() || marcaModelo.isNotBlank()
}

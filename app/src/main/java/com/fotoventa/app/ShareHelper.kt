package com.fotoventa.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

/** Compartir / copiar / guardar (SPEC-05). Nada se sube a ningún servidor. */
object ShareHelper {

    fun shareToWhatsApp(context: Context, photoUri: Uri?, text: String): Boolean {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_TEXT, text)
            photoUri?.let {
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            setPackage("com.whatsapp")
        }
        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("FotoVenta", text))
        Toast.makeText(context, "Texto copiado", Toast.LENGTH_SHORT).show()
    }

    /** Guarda una imagen del anuncio completo (foto + texto) en la galería. */
    fun saveAdImage(context: Context, view: View, fileName: String): Uri? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/FotoVenta"
                    )
                }
                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                ) ?: return null
                context.contentResolver.openOutputStream(uri)?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                Toast.makeText(context, "Imagen guardada en galería", Toast.LENGTH_SHORT).show()
                uri
            } else {
                @Suppress("DEPRECATION")
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "FotoVenta"
                )
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, fileName)
                FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                Toast.makeText(context, "Imagen guardada en galería", Toast.LENGTH_SHORT).show()
                Uri.fromFile(file)
            }
        } catch (_: Exception) {
            Toast.makeText(context, "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show()
            null
        }
    }
}

# SPEC-04 — Premium $299

## Objetivo

Vender una sola cosa: quitar ads y quitar el límite, para siempre.

## La idea

- **$299 MXN, una sola compra, de por vida. No es suscripción.**
- Es caro a propósito: nadie lo compra, todos ven ads.
- No se baja el precio ni se hacen ofertas sin aprobación de Miguel.

## Qué quita Premium

- Sin AdMob Banner.
- Sin AdMob Interstitial.
- Sin límite de 3 anuncios/día.
- Sin leyenda "Hecho con FotoVenta" en el anuncio final.

## Qué NO cambia

- El flujo es el mismo. Premium no agrega funciones, fotos ni plantillas.
- El OCR, la plantilla y el compartir son idénticos en gratis y Premium.

## Implementación

- Google Play Billing, compra única (one-time purchase).
- Producto configurado en Google Play Console. Su ID va en `PremiumManager.kt` / `local.properties`, no hardcodeado en un commit público.
- Al confirmar la compra: flag `premium = true` en SharedPreferences.
- Al abrir la app: leer el flag y, si hace falta, verificar la compra con Play Billing. Si la verificación dice que no es Premium, el flag se apaga.
- Botón "Quitar ads — $299" visible en gratis, en la pantalla principal y en la pantalla de límite. En Premium no aparece.

## Pantalla de compra

- Título: "FotoVenta Premium".
- Texto: "Sin anuncios, sin límite de anuncios por día, sin leyenda. Pago único de $299, para siempre."
- Botón: "Comprar por $299".
- Después de comprar: "Premium activado" y desaparecen ads y límite de inmediato, sin reiniciar la app.

## Reglas

- Una sola compra por cuenta de Google Play. Reinstalar la app restaura Premium con la misma cuenta (restaurar compras en Play Billing).
- No guardar tarjetas ni datos de pago en la app. Todo lo cobra Google Play.
- Si la compra falla o se cancela, no se activa Premium y no se cobra nada. Avisar "Compra cancelada" y seguir en gratis.
- No prometer reembolso en la app: eso lo maneja Google Play.

## Criterios de aceptación

1. En gratis, el botón de Premium muestra $299 y abre el flujo de Play Billing.
2. Compra confirmada: `premium = true`, desaparecen Banner, Interstitial, límite y leyenda, sin reiniciar.
3. Compra cancelada: sigue en gratis, sin cambios.
4. Reinstalar con la misma cuenta de Google restaura Premium.
5. Sin internet no se puede comprar: avisar y seguir en gratis.

## Fuera de alcance

- Suscripción mensual/anual.
- Precio distinto por país o por promoción.
- Premium familiar o por código.

# FotoVenta

FotoVenta convierte 3 fotos en un anuncio listo para vender un carro por WhatsApp.

El usuario toma la foto del carro, y si quiere agrega una hoja escrita y una hoja de specs/anuncio. La app lee el texto con OCR, arma el anuncio con foto, precio, km, ubicación y WhatsApp, y lo comparte en un toque.

## El negocio, en una línea

**AdMob es el negocio. Premium es solo para quitar ads.**

- Gratis: 3 anuncios al día, con ads y con la leyenda "Hecho con FotoVenta".
- Premium: $299 MXN, una sola compra, de por vida. Sin ads, sin límite, sin leyenda.
- El precio de $299 es caro a propósito: casi nadie lo compra, casi todos ven ads.

## Flujo real

1. El usuario abre la app.
2. Toma la foto del carro (obligatoria).
3. Toma la foto de la hoja escrita (opcional) o la salta.
4. Toma la foto de specs/anuncio (opcional) o la salta.
5. La app hace OCR del texto, el usuario lo corrige si hace falta.
6. Ve el anuncio listo: foto grande arriba, texto abajo.
7. Comparte por WhatsApp, copia el texto o guarda la imagen.
8. Si ya hizo 3 anuncios ese día, ve un anuncio de AdMob para seguir.

Si salta las fotos 2 y 3, va directo a la plantilla genérica con campos vacíos para llenar a mano.

## Stack

- Android, Kotlin, 100% offline excepto ads y billing.
- ML Kit Text Recognition (on-device) para OCR.
- AdMob: Banner en pantallas principales + Interstitial al llegar al límite.
- Google Play Billing: compra única de $299 MXN, de por vida.
- Sin cuentas, sin servidor, sin base de datos en la nube.

## Estructura del proyecto

```
FotoVenta/
├── README.md
├── PLAN.md
├── RULES.md
├── ESTADO.md
├── SPECS/
│   ├── SPEC-01-Flujo-3-Fotos.md
│   ├── SPEC-02-OCR.md
│   ├── SPEC-03-Monetizacion.md
│   ├── SPEC-04-Premium.md
│   └── SPEC-05-Anuncio-final.md
└── app/                    # código Android (Kotlin)
    ├── MainActivity.kt
    ├── CameraFragment.kt
    ├── OCRFragment.kt
    ├── TemplateFragment.kt
    ├── ResultFragment.kt
    ├── AdManager.kt
    ├── PremiumManager.kt
    ├── LimitManager.kt
    └── ShareHelper.kt
```

## Cómo correrlo

1. Abrir el proyecto en Android Studio.
2. Poner tu AdMob App ID en `AndroidManifest.xml` y tus IDs de Banner e Interstitial en `AdManager.kt`.
3. Configurar el producto de compra única de $299 MXN en Google Play Console y poner su ID en `PremiumManager.kt`.
4. Correr en un dispositivo o emulador con cámara.
5. Para probar el flujo gratis no necesitas cuenta de AdMob ni de Play Billing: el límite de 3/día y el OCR funcionan offline.

> Nota: los IDs reales de AdMob y de Play Billing no van en este repo público. Van en `local.properties` o en la configuración de build. Ver `RULES.md`.

## Specs

- [SPEC-01 Flujo 3 fotos](SPECS/SPEC-01-Flujo-3-Fotos.md)
- [SPEC-02 OCR](SPECS/SPEC-02-OCR.md)
- [SPEC-03 Monetización](SPECS/SPEC-03-Monetizacion.md)
- [SPEC-04 Premium](SPECS/SPEC-04-Premium.md)
- [SPEC-05 Anuncio final](SPECS/SPEC-05-Anuncio-final.md)

## Estado

Ver [ESTADO.md](ESTADO.md). Al 9 de noviembre de 2026: specs escritas, código aún no empezado. Siguiente paso: Slice 1, flujo de 3 fotos sin OCR ni ads.

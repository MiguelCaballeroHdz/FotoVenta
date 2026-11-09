# PLAN — FotoVenta

Roadmap por fases y por slices. Cada slice se trabaja en rama, se verifica y se sube en PR. No se toca `main` directo.

## MVP

Android Kotlin, 100% offline excepto ads y billing, ML Kit para OCR, AdMob, Play Billing.

Flujo: abrir app → foto carro → hoja escrita o saltar → OCR o llenado manual → anuncio listo → compartir por WhatsApp → si ya hizo 3 ese día, ve anuncio de AdMob para seguir.

Fuera del MVP: cuentas de usuario, servidor, historial en la nube, edición avanzada de imagen, más de un vehículo por anuncio, publicación automática en Marketplace.

## Fases

### Fase 0 — Setup
- Repo en GitHub, estructura Android, `README.md`, `PLAN.md`, `RULES.md`, `ESTADO.md`, `SPECS/`.
- Permisos de cámara y almacenamiento.
- Navegación base en `MainActivity.kt`.
- **Criterio:** la app abre, navega entre pantallas vacías y no crashea.

### Fase 1 — Flujo 3 fotos (SPEC-01)
- Slice 1: `CameraFragment.kt` — foto del carro, obligatoria, con retomar.
- Slice 2: pantallas 2 y 3 opcionales + botón Saltar → plantilla genérica.
- Slice 3: `TemplateFragment.kt` — campos vacíos (precio, km, ubicación, WhatsApp, descripción).
- **Criterio:** se puede completar el flujo con solo la foto del carro y llegar a un anuncio con texto manual.

### Fase 2 — OCR (SPEC-02)
- Slice 4: `OCRFragment.kt` + ML Kit Text Recognition.
- Texto impreso: automático, sin pantalla extra.
- Texto manuscrito: automático + pantalla de corrección antes de generar.
- Si el OCR falla o sale vacío: permitir editar el texto a mano, nunca bloquear.
- **Criterio:** una hoja impresa se lee bien en el primer intento; una manuscrita se puede corregir y generar.

### Fase 3 — Anuncio final y compartir (SPEC-05)
- Slice 5: `ResultFragment.kt` — foto grande arriba, texto abajo.
- Slice 6: `ShareHelper.kt` — Compartir WhatsApp / Copiar / Guardar imagen.
- Leyenda "Hecho con FotoVenta" solo en gratis.
- **Criterio:** el anuncio generado se comparte por WhatsApp con foto + texto, y se puede guardar como imagen.

### Fase 4 — Monetización (SPEC-03)
- Slice 7: `LimitManager.kt` — contador local de 3 anuncios/día, reinicia a medianoche local.
- Slice 8: `AdManager.kt` — Banner en pantallas principales + Interstitial al llegar al límite.
- Botón "Ver anuncio para seguir" desbloquea 1 anuncio extra.
- **Criterio:** al 4.º anuncio del día aparece el interstitial y no se puede generar sin verlo (salvo Premium).

### Fase 5 — Premium (SPEC-04)
- Slice 9: `PremiumManager.kt` — Play Billing, compra única $299 MXN, de por vida.
- Flag `premium = true` en SharedPreferences al confirmar la compra.
- Premium: sin ads, sin límite, sin leyenda.
- **Criterio:** con Premium comprado (o simulado en pruebas) no aparece ningún ad ni límite; sin Premium, todo sigue igual.

### Fase 6 — Pulido y publicación
- Pruebas en dispositivo real con fotos reales (impresas y manuscritas).
- Textos, emojis y plantilla revisados con un anuncio real.
- Capturas, descripción de Play Store y release interno.
- **Criterio:** un usuario nuevo vende su carro con la app sin ayuda, de principio a fin.

## Orden de código

1. `MainActivity.kt` — navegación
2. `CameraFragment.kt` — toma foto carro
3. `OCRFragment.kt` — recibe foto hoja → ML Kit → texto editable
4. `TemplateFragment.kt` — plantilla genérica con campos vacíos
5. `ResultFragment.kt` — junta foto + texto → genera anuncio
6. `AdManager.kt` — AdMob Interstitial + Banner
7. `PremiumManager.kt` — Play Billing $299
8. `LimitManager.kt` — contador 3/día
9. `ShareHelper.kt` — compartir a WhatsApp

## Qué sigue

1. Aprobar estos MD y subirlos a GitHub.
2. Slice 1: `CameraFragment.kt` + navegación.
3. Probar en el POCO con fotos reales.

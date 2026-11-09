# ESTADO — FotoVenta

Última actualización: 9 de noviembre de 2026.

## Dónde vamos

- Specs y archivos MD listos, en local (`~/workspace/FotoVenta/`) y subidos a GitHub.
- Código Android de los slices 1-9 escrito en local, sin verificar con build, y subido a GitHub.
- Repo de GitHub creado por Miguel el 9 de noviembre de 2026: [MiguelCaballeroHdz/FotoVenta](https://github.com/MiguelCaballeroHdz/FotoVenta), público, rama main. Venía solo con el `.gitignore` de Android de GitHub, que se dejó como está.
- Build `assembleDebug` en esta VM: falla. Gradle no logra comunicarse con su daemon (`DaemonConnectionException`, "Unexpected type tag 109"), con daemon y con `--no-daemon`, incluso bajando el heap a 1024m. La VM tenía ~400 MB libres. Es la pared de esta VM, igual que el timeout de Maven de GPS Quest.
- Verificación estática hecha en local: XML bien formado, todos los IDs de binding existen en los layouts, todos los strings referenciados están definidos, llaves y paréntesis balanceados en los 10 archivos Kotlin.

## Slices

| Slice | Qué | Estado |
|---|---|---|
| 0 | Setup: repo, estructura, MDs y specs | Hecho (MDs) / Pendiente subir a GitHub |
| 1 | `CameraFragment.kt` + navegación | Código en local, sin verificar build |
| 2 | Pantallas 2 y 3 opcionales + Saltar | Código en local, sin verificar build |
| 3 | `TemplateFragment.kt` | Código en local, sin verificar build |
| 4 | `OCRFragment.kt` + ML Kit | Código en local, sin verificar build |
| 5 | `ResultFragment.kt` | Código en local, sin verificar build |
| 6 | `ShareHelper.kt` | Código en local, sin verificar build |
| 7 | `LimitManager.kt` (3/día) | Código en local, sin verificar build |
| 8 | `AdManager.kt` (Banner + Interstitial) | Código en local, sin verificar build |
| 9 | `PremiumManager.kt` ($299) | Código en local, sin verificar build |

## Decisiones tomadas

- 9 nov 2026: AdMob es el negocio, Premium es solo para quitar ads.
- 9 nov 2026: Premium $299 MXN, compra única de por vida. Precio alto a propósito: casi nadie lo compra, casi todos ven ads.
- 9 nov 2026: Límite gratis 3 anuncios/día, contador local.
- 9 nov 2026: OCR con ML Kit Text Recognition, on-device. Manuscrita con pantalla de corrección.
- 9 nov 2026: App 100% offline excepto ads y billing.

## Pendientes de Miguel

- [ ] Abrir el proyecto en Android Studio en su PC y correr `assembleDebug`; en esta VM el build no corre.
- [ ] Probar en el POCO con fotos reales cuando esté el Slice 1.
- [ ] IDs reales de AdMob y producto de Play Billing (se ponen en `local.properties`, no en el repo).

## Riesgos

- OCR manuscrito: puede fallar con letra poco legible. Mitigación: pantalla de corrección + edición manual siempre disponible.
- AdMob: sin IDs reales no se pueden probar ads reales; usar IDs de prueba en desarrollo.
- Play Billing: la compra de $299 solo se puede probar con tester de licencia en Play Console.

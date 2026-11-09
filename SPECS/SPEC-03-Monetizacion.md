# SPEC-03 — Monetización (AdMob y límites)

## Objetivo

Que la app gane con ads. Premium existe solo para quitarlos.

## Límite gratis

- **3 anuncios gratis por día.** Contador local, en SharedPreferences.
- Cuenta cada vez que el usuario genera un anuncio (botón Generar), no cada foto tomada.
- Reinicia a medianoche, hora local del dispositivo.
- Premium: sin límite.

## Al llegar al límite

- Al intentar el 4.º anuncio del día, aparece un AdMob Interstitial.
- Después del interstitial, botón **"Ver anuncio para seguir"**.
- Al verlo, se desbloquea **1 anuncio extra** ese día. Cada anuncio extra cuesta un interstitial.
- No hay forma de saltar el interstitial en gratis. Cerrar la app no resetea el contador.
- Premium: nunca aparece el interstitial ni el botón.

## Banner

- AdMob Banner en pantallas principales: inicio, flujo de fotos y anuncio final.
- No tapa botones. Va abajo, fuera del área de acción.
- Premium: no aparece ningún banner.
- En desarrollo se usan IDs de prueba de AdMob. Los IDs reales van en `local.properties`, no en el repo.

## Premium (resumen)

- Sin ads, sin límite, sin leyenda "Hecho con FotoVenta".
- Detalle completo en SPEC-04.

## Reglas

- El contador es local. Cambiar la fecha del teléfono no es un caso que el MVP tenga que prevenir.
- Si AdMob no carga (sin internet, sin IDs), el usuario gratis no se queda bloqueado para siempre: se le avisa "No se pudo cargar el anuncio, inténtalo de nuevo" y puede reintentar. No se le regala el anuncio extra sin ver el ad.
- Nunca mostrar un interstitial en medio de tomar una foto. Solo al intentar generar pasando el límite.
- No pedir datos personales para ver ads.

## Criterios de aceptación

1. Días 1–3: genera sin interstitial.
2. 4.º intento: aparece interstitial + botón "Ver anuncio para seguir".
3. Después de verlo: se genera 1 anuncio extra.
4. 5.º intento: vuelve a pedir interstitial.
5. Banner visible en gratis, invisible en Premium.
6. Sin internet: el flujo gratis hasta 3/día sigue funcionando; el extra avisa que no cargó el anuncio.

## Fuera de alcance

- Anuncios recompensados de video (Rewarded) como formato aparte: el "ver anuncio para seguir" es el interstitial de esta spec.
- Suscripciones, paquetes de anuncios extra, o pagar por anuncio individual.
- Analytics de ads en el MVP.

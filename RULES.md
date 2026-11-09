# RULES — FotoVenta

Reglas para Muse / Code al trabajar en este proyecto. Si una regla choca con un atajo, gana la regla.

## Trabajo por slices

- Trabajar en ramas y abrir PRs para revisión. No tocar `main` directo.
- Un slice a la vez, en el orden de `PLAN.md`. No adelantar monetización antes de que el flujo funcione.
- Cada slice termina con verificación real: qué se probó, en qué dispositivo, qué salió.
- Si algo falla en el build o en el dispositivo, decirlo con el error exacto. No reportar como hecho lo que no se verificó.

## Producto

- El MVP es el de `PLAN.md`. Nada fuera del MVP entra sin aprobación de Miguel.
- AdMob es el negocio, Premium es solo para quitar ads. No invertir esa lógica.
- Premium es $299 MXN, una sola compra, de por vida. No es suscripción. No bajar el precio sin aprobación.
- La app es 100% offline excepto ads y billing. No agregar cuentas, servidor ni base de datos en la nube.

## UI

- No tocar la UI sin permiso de Miguel, salvo lo que el slice en curso pida explícitamente.
- No rediseñar pantallas por gusto. Si algo de UI estorba al flujo, proponerlo antes de cambiarlo.
- Los textos del anuncio final son los de SPEC-05. No cambiar emojis, orden ni leyenda sin aprobación.

## Código

- Kotlin, Android. Sin librerías nuevas sin justificarlas en el PR.
- OCR solo con ML Kit Text Recognition on-device. No mandar fotos a ningún servidor.
- Contador de 3/día en local (SharedPreferences). No usar fecha del servidor.
- Flag Premium en SharedPreferences (`premium = true`) solo cuando Play Billing confirma la compra.
- No guardar fotos del usuario fuera de la app sin su acción explícita de compartir/guardar.

## Secretos

- IDs reales de AdMob y de Play Billing no van en el repo público.
- Van en `local.properties` o en configuración de build, nunca hardcodeados en un commit.
- No subir capturas con datos reales de un carro (placas, teléfono) sin permiso.

## Specs

- Si el código choca con una spec, la spec manda hasta que Miguel la cambie.
- Todo cambio de spec se anota en `ESTADO.md` con fecha.
- No inventar precios, límites ni textos: si falta un dato, preguntar o dejarlo marcado como pendiente en la spec.

# SPEC-02 — OCR (de foto a texto)

## Objetivo

Leer el texto de las hojas (pantallas 2 y 3) sin que el usuario tenga que escribirlo.

## Librería

- ML Kit Text Recognition, on-device, sin internet.
- No se manda ninguna foto a ningún servidor.

## Casos

### Texto impreso
- Automático: la app lee el texto y lo pasa directo a los campos del anuncio.
- No se muestra pantalla de corrección obligatoria. El usuario puede editar el texto después, en la plantilla o en el anuncio final, si algo salió mal.

### Texto manuscrito
- Automático + pantalla de corrección antes de generar.
- La pantalla muestra: la foto de la hoja arriba y el texto leído abajo, editable.
- Botones: "El texto está bien" (sigue) y "Editar" (el campo ya es editable, no es otra pantalla).
- Si el usuario corrige, se usa el texto corregido. El OCR original no se vuelve a aplicar.

### Si el OCR falla
- Falla = no se detecta texto, o el texto sale vacío o ilegible.
- Nunca bloquear el flujo. Mostrar: "No pude leer la hoja, escribe el texto a mano" y abrir el campo editable con la foto visible al lado.
- El usuario puede escribir todo a mano y seguir.

## Qué extraer

La app no necesita entender el texto, solo pasarlo. Pero si detecta estos datos en el texto, los propone en los campos correctos y el resto va a descripción:

- Precio: número con $ o "precio".
- Km / kilometraje: número + "km".
- Año: 4 dígitos (1980–2026).
- Ubicación: texto libre.
- WhatsApp / teléfono: 10 dígitos.

Si la detección se equivoca, el usuario lo corrige en la pantalla de corrección o en la plantilla. No hay validación que bloquee.

## Reglas

- OCR siempre local. Si ML Kit no está disponible en el dispositivo, el flujo cae a entrada manual, nunca crashea.
- Una foto, un OCR. No procesar las dos hojas a la vez.
- El texto del OCR nunca se guarda como definitivo sin que el usuario lo vea (en manuscrito) o pueda editarlo (en impreso).

## Criterios de aceptación

1. Hoja impresa legible: el texto aparece en los campos sin escribir nada.
2. Hoja manuscrita legible: aparece la pantalla de corrección con el texto editable.
3. Hoja en blanco o foto borrosa: aparece el aviso y el campo manual, sin bloquear.
4. Texto corregido a mano es el que llega al anuncio final.
5. Todo funciona sin internet.

## Fuera de alcance

- OCR de placas, INE o documentos oficiales.
- Traducción o corrección automática de ortografía.
- Leer texto de la foto del carro (pantalla 1).

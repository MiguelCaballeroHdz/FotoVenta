# SPEC-01 — Flujo 3 fotos

## Objetivo

Que un usuario pase de abrir la app a tener un anuncio listo en menos de 2 minutos, con solo la foto del carro si así lo quiere.

## Pantallas

### Pantalla 1 — Foto carro (obligatoria)
- Cámara a pantalla completa, botón grande de tomar foto.
- Después de tomarla: vista previa con "Usar esta foto" y "Retomar".
- Sin esta foto no se puede avanzar. No hay forma de saltarla.
- La foto se guarda local, solo dentro de la app.

### Pantalla 2 — Hoja escrita (opcional)
- Texto guía: "Toma foto de tu hoja con precio, km y datos, o salta".
- Botón "Tomar foto" y botón "Saltar".
- Si toma foto: pasa a OCR (SPEC-02).
- Si salta: no se pierde nada, sigue a la pantalla 3.

### Pantalla 3 — Specs / anuncio (opcional)
- Texto guía: "Toma foto de tus specs o de otro anuncio, o salta".
- Botón "Tomar foto" y botón "Saltar".
- Si toma foto: pasa a OCR (SPEC-02).
- Si salta en la 2 y en la 3: va directo a la plantilla genérica.

## Botones

- **Saltar:** va a la siguiente pantalla, o a la plantilla genérica si ya es la última foto opcional. Nunca borra la foto del carro.
- **Generar:** aparece cuando ya hay foto del carro y el usuario terminó las pantallas 2 y 3 (con foto o saltadas). Une foto + texto y pasa al anuncio final (SPEC-05).
- **Atrás:** vuelve a la pantalla anterior sin perder las fotos ya tomadas.

## Plantilla genérica

- Si el usuario saltó las fotos 2 y 3, llega a `TemplateFragment.kt` con campos vacíos:
  - Precio, km, año, modelo/marca, ubicación, WhatsApp, descripción.
- Todos los campos son opcionales excepto que el anuncio necesita al menos precio o descripción para generarse. Si están todos vacíos, el botón Generar avisa y no avanza.

## Reglas

- El flujo nunca pide cuenta, registro ni internet (excepto ads/billing, que van aparte).
- Las fotos 2 y 3 son opcionales, siempre. Ningún cambio posterior las vuelve obligatorias sin aprobar la spec.
- Si el usuario cierra la app a mitad del flujo, no se guarda nada a medias. Al volver, empieza de nuevo.
- Una foto por pantalla. No galería múltiple en el MVP.

## Criterios de aceptación

1. Con solo la foto del carro + Saltar + Saltar, se llega a la plantilla genérica.
2. Con las 3 fotos, se llega al OCR y luego al anuncio.
3. Retomar la foto del carro reemplaza la anterior, no duplica.
4. El botón Generar no aparece sin foto del carro.
5. Atrás no borra fotos ya tomadas.

## Fuera de alcance

- Elegir foto de galería, editar/recortar foto, filtros.
- Más de un carro por anuncio.
- Guardar borradores.

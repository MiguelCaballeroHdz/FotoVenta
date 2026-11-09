# SPEC-05 — Anuncio final

## Objetivo

Un anuncio que se vea bien en WhatsApp y que se comparta en un toque.

## Layout

- **Foto grande arriba:** la foto del carro (pantalla 1), a todo lo ancho.
- **Texto abajo:** el texto del anuncio, con emojis, precio, km, ubicación y WhatsApp.
- **Leyenda al final (solo gratis):** "Hecho con FotoVenta".
- **Botones abajo:** Compartir WhatsApp / Copiar / Guardar imagen.

## Texto del anuncio

Orden fijo:

```
🚗 [Marca / modelo / año]
💰 Precio: $[precio]
📍 Ubicación: [ubicación]
🛣️ Kilometraje: [km] km
📱 WhatsApp: [número]

[Descripción]

Hecho con FotoVenta   ← solo en gratis
```

- Los campos vacíos no aparecen. Si no hay km, no sale la línea de km.
- Si no hay descripción, no sale el párrafo de descripción.
- Los emojis y el orden son los de arriba. No cambiarlos sin aprobación.

## Botones

### Compartir WhatsApp
- Abre WhatsApp con el texto del anuncio + la foto del carro adjunta.
- Si WhatsApp no está instalado: avisar "WhatsApp no está instalado" y ofrecer Copiar.
- No publica nada solo. El usuario elige a quién mandarlo en WhatsApp.

### Copiar
- Copia solo el texto del anuncio al portapapeles.
- Aviso: "Texto copiado".

### Guardar imagen
- Guarda una imagen del anuncio completo (foto + texto) en la galería del teléfono.
- Aviso con dónde quedó guardada.
- La imagen guardada lleva la leyenda solo en gratis, igual que en pantalla.

## Reglas

- El anuncio final siempre se puede editar antes de compartir: tocar el texto lo vuelve editable. Lo editado es lo que se comparte.
- La foto que se comparte es la del carro, sin filtros ni marcas, salvo la leyenda en la imagen guardada (solo gratis).
- No se sube el anuncio a ningún servidor. Compartir es acción del usuario.

## Criterios de aceptación

1. El anuncio muestra la foto del carro arriba y el texto con precio, km, ubicación y WhatsApp abajo.
2. Los campos vacíos no dejan líneas en blanco.
3. Compartir por WhatsApp manda foto + texto.
4. Copiar copia el texto exacto que se ve.
5. Guardar imagen guarda foto + texto en galería.
6. En gratis aparece "Hecho con FotoVenta"; en Premium, no.

## Fuera de alcance

- Publicar directo en Facebook Marketplace o Mercado Libre.
- Plantillas de diseño múltiples.
- Edición de foto (recorte, filtros, texto sobre la foto).

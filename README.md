# 📍 DistanceMap

Una aplicación que permite visualizar pedidos en un mapa, calcular cuál está más cerca de tu ubicación actual y simular su entrega.

---

## 🚀 Funcionalidades

-  Obtener la ubicación actual del dispositivo (puede ser simulada).
-  Mostrar dos coordenadas GPS predefinidas que representan pedidos.
-  Calcular cuál pedido está más cerca de la ubicación actual.
-  Mostrar en pantalla:
    -  La ubicación actual.
    - Las coordenadas de los pedidos.
    - El pedido que debe entregarse primero.
-  Botón para simular la entrega del pedido más cercano, y mostrar el siguiente pedido por atender.

---

## 🛠️ Instrucciones de uso

1. **Abrir la aplicación.**
2. **Aceptar los permisos de GPS** al iniciarse.
3. **Tocar el ícono superior** para centrar el mapa en tu ubicación actual.
4. **Haz clic sobre el mapa** para agregar o eliminar marcadores.
5. Una vez tengas **más de dos marcadores**, se habilitará un botón que te permitirá:
   - Ver un listado de todos los marcadores.
   - Visualizar tu posición actual.
   - Consultar la distancia desde tu ubicación a cada marcador.
6. En la vista de mapa:
   - 🔴 **Marcador rojo**: representa el punto más cercano.
   - 🔵 **Marcadores azules**: los siguientes pedidos en la fila.
   - Al simular una entrega, el más cercano se actualiza automáticamente.

---

## 🧪 Pruebas realizadas

- 🧭 **Simulación de ubicación** usando el emulador de Android para probar múltiples posiciones geográficas.
- 📍 Se colocaron manualmente puntos en el mapa y se comparó la distancia calculada con la herramienta de medición de Google Maps para validación.
- 🔄 Durante las pruebas:
  - Se pintaron los puntos de origen, destino y la ubicación actual.
  - Se validó visualmente que el punto identificado como más cercano fuera correcto.
  - El flujo de entrega y actualización de marcador más cercano funcionó como se esperaba.

## 🎥 Video 

[video_comprimido.webm](video_comprimido.webm)
## 🤖 APK para pruebas

[testApp.apk](testApp.apk)
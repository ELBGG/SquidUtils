# Configuración de SquidUtils

Este directorio contiene los archivos de configuración del mod SquidUtils.

## waitinghud.json

Archivo de configuración para personalizar el HUD de la sala de espera.

### Opciones disponibles:

#### **colors** - Colores en formato hexadecimal (#RRGGBB)
- `mainText`: Color del texto principal ("Esperando jugadores...")
- `mainTextShadow`: Color de la sombra del texto principal
- `counter`: Color del contador de jugadores (X/Y)
- `counterShadow`: Color de la sombra del contador
- `startingText`: Color del texto cuando la sala está completa ("Iniciando...")

#### **textScale** - Escala de los textos
- `mainText`: Escala del texto principal (por defecto: 2.0)
- `counter`: Escala del contador (por defecto: 1.5)

#### **position** - Posición de los elementos
- `mainTextY`: Posición vertical del texto principal como porcentaje de la altura de la pantalla (0.0 - 1.0)
- `counterOffsetY`: Desplazamiento en píxeles del contador desde el texto principal

### Ejemplo de configuración:

```json
{
  "colors": {
    "mainText": "#FFFFFF",
    "mainTextShadow": "#000000",
    "counter": "#FFFFFF",
    "counterShadow": "#000000",
    "startingText": "#55FF55"
  },
  "textScale": {
    "mainText": 2.0,
    "counter": 1.5
  },
  "position": {
    "mainTextY": 0.45,
    "counterOffsetY": 40
  }
}
```

### Cómo aplicar cambios:

1. Edita el archivo `waitinghud.json`
2. Guarda el archivo
3. Reinicia el servidor de Minecraft
4. Los jugadores recibirán la nueva configuración cuando se conecten

### Colores sugeridos (tema Squid Game):

- Rojo: `#D32F2F` o `#C62828`
- Verde: `#55FF55` o `#00E676`
- Blanco: `#FFFFFF`
- Negro: `#000000`

### Notas:

- Los colores se sincronizan automáticamente a los clientes cuando se conectan
- No es necesario que los jugadores tengan archivos de configuración
- Los cambios solo se aplican cuando el servidor se reinicia

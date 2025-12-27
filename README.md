# SquidUtils

Mod de Fabric para Minecraft que proporciona utilidades y APIs para crear experiencias personalizadas en el servidor.

## Tabla de Contenidos

- [Características](#características)
- [APIs Disponibles](#apis-disponibles)
  - [PlayerTitleAPI](#playertitleapi)
  - [BlurEffectAPI](#blureffectapi)
- [Comandos](#comandos)
  - [/squidutils](#squidutils)
  - [/waitingroom](#waitingroom)
- [Configuraciones](#configuraciones)
  - [dead.json](#deadjson)
  - [waitinghud.json](#waitinghudjson)
- [Sistemas](#sistemas)
- [Ejemplos de Uso](#ejemplos-de-uso)

---

## Características

- **Títulos Personalizados**: Muestra títulos y subtítulos personalizados en la pantalla del jugador
- **Efectos de Blur**: Aplica efectos de desenfoque con colores personalizados
- **Sistema de Numeración**: Asigna números a los jugadores automáticamente
- **Mensajes de Muerte Personalizados**: Mensajes y efectos personalizados al morir
- **Comandos Programados al Morir**: Ejecuta comandos automáticamente después de la muerte
- **Sala de Espera**: Sistema de lobby con contador de jugadores
- **Estado de Alma**: Efecto de espectador con inercia al morir

---

## APIs Disponibles

### PlayerTitleAPI

API para mostrar títulos y subtítulos personalizados en la pantalla del jugador.

#### Métodos

```java
// Mostrar título con fade automático de 5 segundos
PlayerTitleAPI.showTitle(ServerPlayerEntity player, String title, String subtitle, int titleColor)

// Mostrar título permanente (hasta ocultarlo manualmente)
PlayerTitleAPI.showTitlePermanent(ServerPlayerEntity player, String title, String subtitle, int titleColor)

// Mostrar título con duración personalizada
PlayerTitleAPI.showTitleWithDuration(ServerPlayerEntity player, String title, String subtitle,
                                     int titleColor, long durationMs)

// Control completo de todos los parámetros
PlayerTitleAPI.showTitle(ServerPlayerEntity player, String title, String subtitle,
                        int titleColor, float offsetX, float offsetY,
                        float titleScale, float subtitleScale,
                        long displayDuration, long fadeOutDuration, boolean autoFade)

// Ocultar título
PlayerTitleAPI.hideTitle(ServerPlayerEntity player)
```

#### Colores Predefinidos

```java
PlayerTitleAPI.COLOR_WHITE        // 0xFFFFFF
PlayerTitleAPI.COLOR_RED          // 0xFF0000
PlayerTitleAPI.COLOR_GREEN        // 0x00FF00
PlayerTitleAPI.COLOR_BLUE         // 0x0000FF
PlayerTitleAPI.COLOR_YELLOW       // 0xFFFF00
PlayerTitleAPI.COLOR_GOLD         // 0xFFAA00
PlayerTitleAPI.COLOR_AQUA         // 0x00FFFF
PlayerTitleAPI.COLOR_DARK_RED     // 0xAA0000
PlayerTitleAPI.COLOR_DARK_GREEN   // 0x00AA00
PlayerTitleAPI.COLOR_DARK_BLUE    // 0x0000AA
PlayerTitleAPI.COLOR_DARK_PURPLE  // 0xAA00AA
PlayerTitleAPI.COLOR_GRAY         // 0xAAAAAA
PlayerTitleAPI.COLOR_DARK_GRAY    // 0x555555
```

#### Ejemplo de Uso

```java
// Mostrar título rojo durante 5 segundos
PlayerTitleAPI.showTitle(player, "¡GAME OVER!", "Mejor suerte la próxima", PlayerTitleAPI.COLOR_RED);

// Mostrar título permanente
PlayerTitleAPI.showTitlePermanent(player, "ESPERANDO", "Jugadores: 5/10", PlayerTitleAPI.COLOR_YELLOW);

// Ocultar título cuando sea necesario
PlayerTitleAPI.hideTitle(player);
```

---

### BlurEffectAPI

API para aplicar efectos de desenfoque en el borde de la pantalla del jugador.

#### Métodos

```java
// Activar blur con valores por defecto
BlurEffectAPI.enableBlur(ServerPlayerEntity player)

// Activar blur con color personalizado
BlurEffectAPI.enableBlur(ServerPlayerEntity player, int hexColor)

// Activar blur con control completo
BlurEffectAPI.enableBlur(ServerPlayerEntity player, float borderWidth, float blurIntensity, int hexColor)

// Activar blur temporalmente (se desactiva automáticamente)
BlurEffectAPI.enableBlurTemporary(ServerPlayerEntity player, long durationMs, int hexColor)

// Activar blur temporal con control completo
BlurEffectAPI.enableBlurTemporary(ServerPlayerEntity player, long durationMs,
                                  float borderWidth, float blurIntensity, int hexColor)

// Desactivar blur
BlurEffectAPI.disableBlur(ServerPlayerEntity player)

// Actualizar solo el color
BlurEffectAPI.updateColor(ServerPlayerEntity player, int hexColor)
```

#### Colores Predefinidos

```java
BlurEffectAPI.COLOR_RED       // 0xFF0000
BlurEffectAPI.COLOR_GREEN     // 0x00FF00
BlurEffectAPI.COLOR_BLUE      // 0x0000FF
BlurEffectAPI.COLOR_YELLOW    // 0xFFFF00
BlurEffectAPI.COLOR_CYAN      // 0x00FFFF
BlurEffectAPI.COLOR_MAGENTA   // 0xFF00FF
BlurEffectAPI.COLOR_ORANGE    // 0xFF6600
BlurEffectAPI.COLOR_PURPLE    // 0x9900FF
BlurEffectAPI.COLOR_PINK      // 0xFF69B4
BlurEffectAPI.COLOR_POISON    // 0x00FF33
BlurEffectAPI.COLOR_FIRE      // 0xFF4400
BlurEffectAPI.COLOR_ICE       // 0x00DDFF
BlurEffectAPI.COLOR_DARK      // 0x333333
```

#### Ejemplo de Uso

```java
// Activar blur rojo durante 5 segundos
BlurEffectAPI.enableBlurTemporary(player, 5000, BlurEffectAPI.COLOR_RED);

// Activar blur permanente
BlurEffectAPI.enableBlur(player, BlurEffectAPI.COLOR_POISON);

// Desactivar blur
BlurEffectAPI.disableBlur(player);

// Blur personalizado (ancho: 0.3, intensidad: 1.5)
BlurEffectAPI.enableBlur(player, 0.3F, 1.5F, 0xFF00AA);
```

---

## Comandos

### /squidutils

Comando principal para gestionar los sistemas del mod. **Requiere nivel de permisos 2 (OP)**.

#### Subcomandos

```bash
# Sistema de Numeración de Jugadores
/squidutils numberplayer start    # Inicia el sistema de numeración
/squidutils numberplayer stop     # Detiene el sistema de numeración
/squidutils numberplayer stats    # Muestra estadísticas del sistema

# Sistema de Mensajes de Muerte
/squidutils deathmessage start    # Activa mensajes de muerte personalizados
/squidutils deathmessage stop     # Desactiva mensajes de muerte personalizados
```

#### Ejemplos

```bash
# Iniciar numeración de jugadores
/squidutils numberplayer start

# Ver estadísticas
/squidutils numberplayer stats

# Activar sistema de muerte personalizado
/squidutils deathmessage start
```

---

### /waitingroom

Comando para gestionar la sala de espera. **Requiere nivel de permisos 2 (OP)**.

#### Subcomandos

```bash
/waitingroom start <maxPlayers>   # Inicia sala de espera (1-100 jugadores)
/waitingroom stop                 # Detiene la sala de espera
/waitingroom status               # Muestra el estado actual
```

#### Ejemplos

```bash
# Iniciar sala de espera para 10 jugadores
/waitingroom start 10

# Ver estado actual
/waitingroom status

# Detener sala de espera
/waitingroom stop
```

---

## Configuraciones

Las configuraciones se encuentran en la carpeta `config/squidutils/` del servidor.

### dead.json

Configuración de comandos que se ejecutan automáticamente cuando un jugador muere.

**Ubicación**: `config/squidutils/dead.json`

#### Estructura

```json
{
  "commands": [
    {
      "delaySeconds": 0,
      "command": "say {player} ha muerto!"
    },
    {
      "delaySeconds": 3,
      "command": "title {player} times 10 70 20"
    },
    {
      "delaySeconds": 3.05,
      "command": "title {player} title {\"text\":\"HAS MUERTO\",\"color\":\"red\",\"bold\":true}"
    },
    {
      "delaySeconds": 3.1,
      "command": "title {player} subtitle {\"text\":\"Mejor suerte la próxima vez\",\"color\":\"gray\"}"
    }
  ]
}
```

#### Parámetros

- **delaySeconds**: Tiempo en segundos desde la muerte del jugador (puede ser decimal, ej: 2.5)
- **command**: Comando a ejecutar (usa `{player}` para el nombre del jugador)

#### Características

- Los comandos solo afectan al jugador que murió
- Se ejecutan en orden según el delay especificado
- Puedes usar cualquier comando de Minecraft
- El placeholder `{player}` se reemplaza automáticamente

#### Ejemplos de Comandos

```json
{
  "commands": [
    {
      "delaySeconds": 0,
      "command": "effect give {player} minecraft:blindness 5 0 true"
    },
    {
      "delaySeconds": 2,
      "command": "playsound minecraft:entity.wither.death master {player} ~ ~ ~ 1 1"
    },
    {
      "delaySeconds": 5,
      "command": "tp {player} 0 100 0"
    },
    {
      "delaySeconds": 6,
      "command": "gamemode spectator {player}"
    }
  ]
}
```

---

### waitinghud.json

Configuración de la interfaz de la sala de espera.

**Ubicación**: `config/squidutils/waitinghud.json`

#### Estructura

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

#### Parámetros

**Colors**:
- `mainText`: Color del texto principal (formato hexadecimal)
- `mainTextShadow`: Color de la sombra del texto principal
- `counter`: Color del contador de jugadores
- `counterShadow`: Color de la sombra del contador
- `startingText`: Color del texto cuando el juego está por empezar

**TextScale**:
- `mainText`: Escala del texto principal (1.0 = tamaño normal)
- `counter`: Escala del contador

**Position**:
- `mainTextY`: Posición vertical del texto principal (0.0 - 1.0)
- `counterOffsetY`: Desplazamiento vertical del contador en píxeles

---

## Sistemas

### NumberPlayerSystem

Asigna números únicos a los jugadores y los muestra en el tablero de jugadores (TAB).

**Auto-inicio**: Activado automáticamente al iniciar el servidor

**Características**:
- Asigna números consecutivos a los jugadores
- Muestra el número en el nombre del TAB
- Formatea los números con ceros a la izquierda (001, 002, etc.)

### CustomDeathMessageSystem

Sistema de mensajes de muerte personalizados con efectos visuales.

**Auto-inicio**: Activado automáticamente al iniciar el servidor

**Características**:
- Mensajes de muerte personalizados
- Efecto de oscurecimiento al morir
- Integración con el sistema de numeración
- Integración con el sistema de comandos programados

### SoulDepartureSystem

Convierte al jugador en espectador con inercia cuando muere.

**Auto-inicio**: Activado automáticamente con CustomDeathMessageSystem

**Características**:
- El jugador se convierte en espectador al morir
- Mantiene la inercia del golpe que lo mató
- Duración de 6.5 segundos (130 ticks)
- Restaura automáticamente al jugador después

### DeadCommandsSystem

Ejecuta comandos programados cuando un jugador muere.

**Auto-inicio**: Activado automáticamente al iniciar el servidor

**Características**:
- Lee configuración desde `config/squidutils/dead.json`
- Ejecuta comandos con delays precisos
- Reemplaza `{player}` por el nombre del jugador
- Los comandos solo afectan al jugador que murió

### WaitingRoomManager

Sistema de sala de espera para lobbies.

**Características**:
- Cuenta jugadores automáticamente
- Muestra HUD personalizado
- Sistema de fases (WAITING, STARTING, GAME_STARTED)
- Configurable mediante comandos

---

## Ejemplos de Uso

### Ejemplo 1: Sistema de Muerte Personalizado

```java
// En tu evento de inicio de juego
public void onGameStart(MinecraftServer server) {
    // Activar sistema de numeración
    NumberPlayerSystem.start(server);

    // Activar mensajes de muerte personalizados
    CustomDeathMessageSystem.activate();

    // Los comandos de dead.json se ejecutarán automáticamente
}
```

### Ejemplo 2: Sala de Espera

```java
// Configurar sala de espera mediante comandos
// /waitingroom start 10

// O desde código
WaitingRoomManager manager = WaitingRoomManager.getInstance();
manager.startWaitingRoom(10); // 10 jugadores máximo
```

### Ejemplo 3: Efectos Personalizados

```java
public void onPlayerDamaged(ServerPlayerEntity player) {
    // Aplicar blur rojo temporal
    BlurEffectAPI.enableBlurTemporary(player, 3000, BlurEffectAPI.COLOR_RED);

    // Mostrar título de advertencia
    PlayerTitleAPI.showTitleWithDuration(player,
        "¡CUIDADO!",
        "Vida baja",
        PlayerTitleAPI.COLOR_DARK_RED,
        2000);
}
```

### Ejemplo 4: Configuración de Comandos de Muerte

Edita `config/squidutils/dead.json`:

```json
{
  "commands": [
    {
      "delaySeconds": 0,
      "command": "playsound minecraft:entity.lightning_bolt.thunder master @a ~ ~ ~ 1 1"
    },
    {
      "delaySeconds": 1,
      "command": "particle minecraft:explosion_emitter ~ ~ ~ 0 0 0 0 1"
    },
    {
      "delaySeconds": 2,
      "command": "title {player} title {\"text\":\"ELIMINADO\",\"color\":\"dark_red\",\"bold\":true}"
    },
    {
      "delaySeconds": 5,
      "command": "tp {player} -100 64 0"
    },
    {
      "delaySeconds": 6,
      "command": "gamemode spectator {player}"
    }
  ]
}
```

---

## Instalación

1. Descarga el archivo `.jar` del mod
2. Colócalo en la carpeta `mods/` de tu servidor/cliente Fabric
3. Inicia el servidor
4. Las configuraciones se generarán automáticamente en `config/squidutils/`

## Uso de la API
En el apartado de repositorios incluir algo tal que asi:
```gradle
repositories {
    maven {
        name = 'Ladysnake Mods'
        url = 'https://maven.ladysnake.org/releases'
        content {
            includeGroup 'io.github.ladysnake'
            includeGroup 'org.ladysnake'
            includeGroupByRegex 'dev\\.onyxstudios.*'
        }
    }

    maven { url 'https://jitpack.io' }
}
```

En el apartado de dependencias incluir algo tal que asi:
```gradle
dependencies {
    modImplementation "com.github.ELBGG:SquidUtils:${squid_version}"
    include "com.github.ELBGG:SquidUtils:${squid_version}"
    modImplementation "org.ladysnake:satin:${satin_version}"
}
```
## Requisitos

- Minecraft 1.21.1
- Fabric Loader
- Fabric API
- Satin API 2.0.0 

---

## Licencia

Todos los derechos reservados por ELBGG. Prohibida su distribución sin autorización.

## Créditos

Desarrollado por ELBGG

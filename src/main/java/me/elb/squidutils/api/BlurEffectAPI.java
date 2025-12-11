package me.elb.squidutils.api;

import me.elb.squidutils.net.BlurEffectPacket;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * API del servidor para controlar el efecto de blur en los bordes de la pantalla del jugador
 */
public class BlurEffectAPI {

    /**
     * Activa el efecto de blur con valores por defecto
     * @param player El jugador al que se le aplicará el efecto
     */
    public static void enableBlur(ServerPlayerEntity player) {
        enableBlur(player, 0.25F, 1.0F, 0xFF00FF); // Magenta por defecto
    }

    /**
     * Activa el efecto de blur con color personalizado
     * @param player El jugador al que se le aplicará el efecto
     * @param hexColor Color en formato 0xRRGGBB
     */
    public static void enableBlur(ServerPlayerEntity player, int hexColor) {
        enableBlur(player, 0.25F, 1.0F, hexColor);
    }

    /**
     * Activa el efecto de blur con parámetros personalizados
     * @param player El jugador al que se le aplicará el efecto
     * @param borderWidth Ancho del borde (0.0 - 0.5, recomendado 0.25)
     * @param blurIntensity Intensidad del blur (0.5 - 2.0, recomendado 1.0)
     * @param hexColor Color en formato 0xRRGGBB
     */
    public static void enableBlur(ServerPlayerEntity player, float borderWidth, float blurIntensity, int hexColor) {
        float r = ((hexColor >> 16) & 0xFF) / 255.0F;
        float g = ((hexColor >> 8) & 0xFF) / 255.0F;
        float b = (hexColor & 0xFF) / 255.0F;
        
        BlurEffectPacket. send(player, true, borderWidth, blurIntensity, r, g, b);
    }

    /**
     * Activa el efecto de blur con colores RGB
     * @param player El jugador al que se le aplicará el efecto
     * @param borderWidth Ancho del borde (0.0 - 0.5)
     * @param blurIntensity Intensidad del blur (0.5 - 2.0)
     * @param r Componente rojo (0.0 - 1.0)
     * @param g Componente verde (0.0 - 1.0)
     * @param b Componente azul (0.0 - 1.0)
     */
    public static void enableBlur(ServerPlayerEntity player, float borderWidth, float blurIntensity, float r, float g, float b) {
        BlurEffectPacket.send(player, true, borderWidth, blurIntensity, r, g, b);
    }

    /**
     * Desactiva el efecto de blur
     * @param player El jugador al que se le desactivará el efecto
     */
    public static void disableBlur(ServerPlayerEntity player) {
        BlurEffectPacket. send(player, false, 0.25F, 1.0F, 1.0F, 0.0F, 1.0F);
    }

    /**
     * Actualiza solo el color del blur sin cambiar otros parámetros
     * @param player El jugador
     * @param hexColor Nuevo color en formato 0xRRGGBB
     */
    public static void updateColor(ServerPlayerEntity player, int hexColor) {
        enableBlur(player, 0.25F, 1.0F, hexColor);
    }

    // Colores predefinidos para facilitar el uso
    public static final int COLOR_RED = 0xFF0000;
    public static final int COLOR_GREEN = 0x00FF00;
    public static final int COLOR_BLUE = 0x0000FF;
    public static final int COLOR_YELLOW = 0xFFFF00;
    public static final int COLOR_CYAN = 0x00FFFF;
    public static final int COLOR_MAGENTA = 0xFF00FF;
    public static final int COLOR_ORANGE = 0xFF6600;
    public static final int COLOR_PURPLE = 0x9900FF;
    public static final int COLOR_PINK = 0xFF69B4;
    public static final int COLOR_POISON = 0x00FF33;
    public static final int COLOR_FIRE = 0xFF4400;
    public static final int COLOR_ICE = 0x00DDFF;
    public static final int COLOR_DARK = 0x333333;
}
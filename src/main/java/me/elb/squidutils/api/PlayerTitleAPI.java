package me.elb.squidutils. api;

import me.elb.squidutils.net.PlayerTitlePacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * API del servidor para mostrar títulos y subtítulos personalizados en la pantalla del jugador
 */
public class PlayerTitleAPI {

    /**
     * Muestra un título con subtítulo en la pantalla del jugador
     * @param player El jugador
     * @param title Texto del título
     * @param subtitle Texto del subtítulo (siempre será blanco)
     * @param titleColor Color del título en formato 0xRRGGBB
     */
    public static void showTitle(ServerPlayerEntity player, String title, String subtitle, int titleColor) {
        showTitle(player, title, subtitle, titleColor, 0.0F, -50.0F, 1.0F);
    }

    /**
     * Muestra un título sin subtítulo
     * @param player El jugador
     * @param title Texto del título
     * @param titleColor Color del título en formato 0xRRGGBB
     */
    public static void showTitle(ServerPlayerEntity player, String title, int titleColor) {
        showTitle(player, title, "", titleColor, 0.0F, -50.0F, 1.0F);
    }

    /**
     * Muestra un título con todas las opciones personalizables
     * @param player El jugador
     * @param title Texto del título
     * @param subtitle Texto del subtítulo
     * @param titleColor Color del título en formato 0xRRGGBB
     * @param offsetX Desplazamiento horizontal desde el centro (negativo = izquierda, positivo = derecha)
     * @param offsetY Desplazamiento vertical desde el centro (negativo = arriba, positivo = abajo)
     * @param scale Escala del texto (1.0 = tamaño normal)
     */
    public static void showTitle(ServerPlayerEntity player, String title, String subtitle, 
                                int titleColor, float offsetX, float offsetY, float scale) {
        PlayerTitlePacket. send(player, true, title, subtitle, titleColor, offsetX, offsetY, scale);
    }

    /**
     * Muestra un título usando objetos Text de Minecraft
     * @param player El jugador
     * @param title Texto del título
     * @param subtitle Texto del subtítulo
     * @param titleColor Color del título en formato 0xRRGGBB
     */
    public static void showTitle(ServerPlayerEntity player, Text title, Text subtitle, int titleColor) {
        showTitle(player, title.getString(), subtitle.getString(), titleColor);
    }

    /**
     * Actualiza solo el texto manteniendo la posición y color
     * @param player El jugador
     * @param title Nuevo texto del título
     * @param subtitle Nuevo texto del subtítulo
     */
    public static void updateText(ServerPlayerEntity player, String title, String subtitle) {
        showTitle(player, title, subtitle, 0xFFFFFF, 0.0F, -50.0F, 1.0F);
    }

    /**
     * Actualiza solo el color del título
     * @param player El jugador
     * @param titleColor Nuevo color en formato 0xRRGGBB
     */
    public static void updateColor(ServerPlayerEntity player, int titleColor) {
        showTitle(player, "", "", titleColor, 0.0F, -50.0F, 1.0F);
    }

    /**
     * Oculta el título y subtítulo
     * @param player El jugador
     */
    public static void hideTitle(ServerPlayerEntity player) {
        PlayerTitlePacket.send(player, false, "", "", 0xFFFFFF, 0.0F, -50.0F, 1.0F);
    }

    // Colores predefinidos para títulos
    public static final int COLOR_WHITE = 0xFFFFFF;
    public static final int COLOR_RED = 0xFF0000;
    public static final int COLOR_GREEN = 0x00FF00;
    public static final int COLOR_BLUE = 0x0000FF;
    public static final int COLOR_YELLOW = 0xFFFF00;
    public static final int COLOR_GOLD = 0xFFAA00;
    public static final int COLOR_AQUA = 0x00FFFF;
    public static final int COLOR_DARK_RED = 0xAA0000;
    public static final int COLOR_DARK_GREEN = 0x00AA00;
    public static final int COLOR_DARK_BLUE = 0x0000AA;
    public static final int COLOR_DARK_PURPLE = 0xAA00AA;
    public static final int COLOR_GRAY = 0xAAAAAA;
    public static final int COLOR_DARK_GRAY = 0x555555;
}
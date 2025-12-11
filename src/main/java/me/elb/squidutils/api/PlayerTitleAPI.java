package me.elb.squidutils.api;

import me.elb.squidutils.net.PlayerTitlePacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerTitleAPI {

    /**
     * Muestra un título con subtítulo con fade automático de 5 segundos
     */
    public static void showTitle(ServerPlayerEntity player, String title, String subtitle, int titleColor) {
        showTitle(player, title, subtitle, titleColor, 0.0F, -80.0F, 2.0F, 1.0F, 5000L, 1000L, true);
    }

    /**
     * Muestra un título sin fade automático (permanente hasta que se oculte manualmente)
     */
    public static void showTitlePermanent(ServerPlayerEntity player, String title, String subtitle, int titleColor) {
        showTitle(player, title, subtitle, titleColor, 0.0F, -80.0F, 2.0F, 1.0F, 0L, 0L, false);
    }

    /**
     * Muestra un título con duración personalizada
     */
    public static void showTitleWithDuration(ServerPlayerEntity player, String title, String subtitle,
                                             int titleColor, long durationMs) {
        showTitle(player, title, subtitle, titleColor, 0.0F, -80.0F, 2.0F, 1.0F, durationMs, 1000L, true);
    }

    /**
     * Control completo de todos los parámetros
     */
    public static void showTitle(ServerPlayerEntity player, String title, String subtitle,
                                 int titleColor, float offsetX, float offsetY,
                                 float titleScale, float subtitleScale,
                                 long displayDuration, long fadeOutDuration, boolean autoFade) {
        PlayerTitlePacket. send(player, true, title, subtitle, titleColor, offsetX, offsetY,
                titleScale, subtitleScale, displayDuration, fadeOutDuration, autoFade);
    }

    public static void hideTitle(ServerPlayerEntity player) {
        PlayerTitlePacket. send(player, false, "", "", 0xFFFFFF, 0.0F, -80.0F, 2.0F, 1.0F, 0L, 0L, false);
    }

    // Colores predefinidos
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
package me.elb.squidutils.api;

import me.elb.squidutils.net.BlurEffectPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BlurEffectAPI {

    private static final Map<UUID, Long> temporaryBlurs = new HashMap<>();

    /**
     * Activa el efecto de blur temporalmente
     * @param player El jugador
     * @param durationMs Duración en milisegundos
     * @param hexColor Color en formato 0xRRGGBB
     */
    public static void enableBlurTemporary(ServerPlayerEntity player, long durationMs, int hexColor) {
        enableBlur(player, hexColor);
        long removalTime = System.currentTimeMillis() + durationMs;
        temporaryBlurs.put(player. getUuid(), removalTime);
    }

    /**
     * Activa el efecto de blur temporalmente con parámetros completos
     */
    public static void enableBlurTemporary(ServerPlayerEntity player, long durationMs,
                                           float borderWidth, float blurIntensity, int hexColor) {
        enableBlur(player, borderWidth, blurIntensity, hexColor);
        long removalTime = System. currentTimeMillis() + durationMs;
        temporaryBlurs.put(player.getUuid(), removalTime);
    }

    /**
     * Debe llamarse cada tick para manejar blur temporales
     * Llama esto desde tu ServerTickEvents
     */
    public static void tick(net.minecraft.server.MinecraftServer server) {
        long now = System.currentTimeMillis();
        Iterator<Map. Entry<UUID, Long>> iterator = temporaryBlurs.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            UUID playerUUID = entry.getKey();
            long scheduledTime = entry.getValue();

            if (now >= scheduledTime) {
                ServerPlayerEntity player = server. getPlayerManager().getPlayer(playerUUID);
                if (player != null) {
                    disableBlur(player);
                }
                iterator.remove();
            }
        }
    }

    public static void enableBlur(ServerPlayerEntity player) {
        enableBlur(player, 0.25F, 1.0F, 0xFF00FF);
    }

    public static void enableBlur(ServerPlayerEntity player, int hexColor) {
        enableBlur(player, 0.25F, 1.0F, hexColor);
    }

    public static void enableBlur(ServerPlayerEntity player, float borderWidth, float blurIntensity, int hexColor) {
        float r = ((hexColor >> 16) & 0xFF) / 255.0F;
        float g = ((hexColor >> 8) & 0xFF) / 255.0F;
        float b = (hexColor & 0xFF) / 255.0F;

        BlurEffectPacket.send(player, true, borderWidth, blurIntensity, r, g, b);
    }

    public static void enableBlur(ServerPlayerEntity player, float borderWidth, float blurIntensity, float r, float g, float b) {
        BlurEffectPacket.send(player, true, borderWidth, blurIntensity, r, g, b);
    }

    public static void disableBlur(ServerPlayerEntity player) {
        BlurEffectPacket.send(player, false, 0.25F, 1.0F, 1.0F, 0.0F, 1.0F);
        temporaryBlurs.remove(player.getUuid());
    }

    public static void updateColor(ServerPlayerEntity player, int hexColor) {
        enableBlur(player, 0.25F, 1.0F, hexColor);
    }

    // Colores predefinidos
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
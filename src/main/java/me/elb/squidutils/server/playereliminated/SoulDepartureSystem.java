package me.elb. squidutils.server.playereliminated;

import me.elb.squidutils.net.SoulStatePacket;
import net.minecraft.server.MinecraftServer;
import net. minecraft.server.network.ServerPlayerEntity;
import net.minecraft. util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sistema "Alma Ida" - El jugador se convierte en espectador con inercia al morir
 */
public class SoulDepartureSystem {

    private static boolean active = false;
    private static final Map<UUID, SoulData> activeSouls = new ConcurrentHashMap<>();

    // Duraciones
    private static final int DELAY_BEFORE_SPECTATOR = 2; // 2 ticks de delay para recibir knockback
    private static final int SOUL_DURATION = 130; // 6.5 segundos en total

    /**
     * Activa el sistema
     */
    public static void activate() {
        active = true;
    }

    /**
     * Desactiva el sistema
     */
    public static void deactivate() {
        active = false;
        activeSouls.clear();
    }

    /**
     * Verifica si está activo
     */
    public static boolean isActive() {
        return active;
    }

    /**
     * Maneja la muerte de un jugador
     */
    public static void onPlayerDeath(ServerPlayerEntity player) {
        if (!active) return;

        UUID uuid = player.getUuid();

        // Guardar el gamemode original
        GameMode originalGameMode = player.interactionManager.getGameMode();

        // Guardar posición y rotación original
        Vec3d position = player.getPos();
        float yaw = player.getYaw();
        float pitch = player.getPitch();

        // Crear datos del alma (sin velocidad inicial, se capturará después del knockback)
        SoulData soulData = new SoulData(
                originalGameMode,
                position,
                Vec3d.ZERO, // Se actualizará después
                yaw,
                pitch,
                -DELAY_BEFORE_SPECTATOR // Empezar en negativo para el delay
        );

        activeSouls.put(uuid, soulData);

        // Restaurar vida ANTES de cambiar a espectador (evita pantalla de muerte)
        player.setHealth(20.0F);
        player.clearStatusEffects();

        // NO cambiar a espectador todavía, esperar al tick para capturar el knockback

        // Notificar al cliente (el fade empieza inmediatamente)
        SoulStatePacket. send(player, true);
    }

    /**
     * Tick del sistema - actualiza los jugadores en estado de alma
     */
    public static void tick(MinecraftServer server) {
        if (!active || activeSouls.isEmpty()) return;

        activeSouls.entrySet().removeIf(entry -> {
            UUID uuid = entry.getKey();
            SoulData soul = entry. getValue();

            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player == null) return true;

            // Incrementar el contador de ticks
            soul.ticksElapsed++;

            // Si aún está en delay (negativo), esperar
            if (soul.ticksElapsed == 0) {
                // Capturar la velocidad después del knockback
                soul.initialVelocity = player.getVelocity();

                // Ahora sí, cambiar a espectador
                player.changeGameMode(GameMode.SPECTATOR);

                // Aplicar la velocidad capturada
                player.setVelocity(soul.initialVelocity);

                return false; // Continuar
            }

            // Si todavía está en fase de delay, no hacer nada más
            if (soul.ticksElapsed < 0) {
                return false;
            }

            // Aplicar la velocidad de inercia con fricción
            if (soul.ticksElapsed > 0) {
                Vec3d inertiaVelocity = soul.initialVelocity. multiply(0.98); // Fricción del 2%
                soul.initialVelocity = inertiaVelocity;

                // Aplicar velocidad al jugador
                player.setVelocity(inertiaVelocity);
            }

            // Si terminó el efecto, restaurar al jugador
            if (soul. ticksElapsed >= SOUL_DURATION) {
                restorePlayer(player, soul);
                return true;
            }

            return false;
        });
    }

    /**
     * Restaura al jugador a su estado normal
     */
    private static void restorePlayer(ServerPlayerEntity player, SoulData soul) {
        // Restaurar gamemode original
        player.changeGameMode(soul.originalGameMode);

        // Restaurar vida
        player.setHealth(20.0F);

        // Detener movimiento
        player.setVelocity(Vec3d.ZERO);

        // Notificar al cliente
        SoulStatePacket.send(player, false);
    }

    /**
     * Verifica si un jugador está en estado de alma
     */
    public static boolean isSoulState(UUID playerUuid) {
        return activeSouls.containsKey(playerUuid);
    }

    /**
     * Cancela manualmente el estado de alma de un jugador
     */
    public static void cancelSoulState(ServerPlayerEntity player) {
        SoulData soul = activeSouls.remove(player.getUuid());
        if (soul != null) {
            restorePlayer(player, soul);
        }
    }

    /**
     * Datos del alma del jugador
     */
    private static class SoulData {
        final GameMode originalGameMode;
        final Vec3d originalPosition;
        Vec3d initialVelocity;
        final float originalYaw;
        final float originalPitch;
        int ticksElapsed;

        SoulData(GameMode originalGameMode, Vec3d originalPosition, Vec3d initialVelocity,
                 float originalYaw, float originalPitch, int ticksElapsed) {
            this.originalGameMode = originalGameMode;
            this.originalPosition = originalPosition;
            this.initialVelocity = initialVelocity;
            this.originalYaw = originalYaw;
            this.originalPitch = originalPitch;
            this.ticksElapsed = ticksElapsed;
        }
    }
}
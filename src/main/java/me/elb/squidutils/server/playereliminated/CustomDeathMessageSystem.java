package me.elb.squidutils.server. playereliminated;

import me.elb.squidutils. net.DeathFadePacket;
import me.elb.squidutils.server.numberplayer.NumberPlayerSystem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Sistema de mensajes de muerte personalizados con efectos
 */
public class CustomDeathMessageSystem {

    private static boolean active = false;

    /**
     * Activa el sistema
     */
    public static void activate() {
        active = true;
        SoulDepartureSystem.activate();
    }

    /**
     * Desactiva el sistema
     */
    public static void deactivate() {
        active = false;
        SoulDepartureSystem.deactivate();
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
    public static void onPlayerDeath(ServerPlayerEntity victim) {
        if (!active) return;

        // Activar efecto de oscurecimiento en el cliente
        DeathFadePacket. send(victim, true);

        // Activar estado de alma (espectador con inercia)
        SoulDepartureSystem.onPlayerDeath(victim);

        // Ejecutar comandos programados
        DeadCommandsSystem.onPlayerDeath(victim);
    }

    /**
     * Construye el mensaje de muerte
     */
    public static Text buildDeathMessage(ServerPlayerEntity victim) {
        String victimName = victim.getName().getString();
        int victimNumber = NumberPlayerSystem.getPlayerNumber(victim.getUuid());

        if (victimNumber >= 0) {
            String formattedNumber = NumberPlayerSystem.formatNumber(victimNumber);
            return Text.literal(String.format("§cJugador §f#%s §7(%s) §celiminado",
                    formattedNumber, victimName));
        } else {
            return Text.literal(String.format("§cJugador §f%s §celiminado", victimName));
        }
    }
}
package me.elb.squidutils.server.playereliminated;

import me.elb.squidutils.server.config.DeadCommandsConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sistema que ejecuta comandos programados cuando un jugador muere
 */
public class DeadCommandsSystem {

    private static boolean active = true;
    private static final Map<UUID, PlayerDeathData> activeDeaths = new ConcurrentHashMap<>();

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
        activeDeaths.clear();
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
        DeadCommandsConfig config = DeadCommandsConfig.getInstance();

        // Si no hay comandos configurados, no hacer nada
        if (config.commands.isEmpty()) return;

        // Crear lista de comandos programados para este jugador
        List<PendingCommand> pendingCommands = new ArrayList<>();
        for (DeadCommandsConfig.ScheduledCommand scheduledCmd : config.commands) {
            // Reemplazar {player} por el nombre del jugador
            String command = scheduledCmd.command.replace("{player}", player.getName().getString());

            // Convertir segundos a ticks (20 ticks = 1 segundo)
            int delayTicks = (int) (scheduledCmd.delaySeconds * 20);

            pendingCommands.add(new PendingCommand(delayTicks, command));
        }

        // Registrar la muerte con sus comandos pendientes
        PlayerDeathData deathData = new PlayerDeathData(player.getName().getString(), pendingCommands);
        activeDeaths.put(uuid, deathData);

        System.out.println("[SquidUtils] Programados " + pendingCommands.size() + " comandos para " + player.getName().getString());
    }

    /**
     * Tick del sistema - ejecuta comandos que llegaron a su delay
     */
    public static void tick(MinecraftServer server) {
        if (!active || activeDeaths.isEmpty()) return;

        activeDeaths.entrySet().removeIf(entry -> {
            UUID uuid = entry.getKey();
            PlayerDeathData deathData = entry.getValue();

            // Incrementar contador de ticks
            deathData.ticksElapsed++;

            // Verificar si hay comandos listos para ejecutar
            Iterator<PendingCommand> iterator = deathData.pendingCommands.iterator();
            while (iterator.hasNext()) {
                PendingCommand cmd = iterator.next();

                if (deathData.ticksElapsed >= cmd.delayTicks) {
                    // Ejecutar el comando
                    executeCommand(server, cmd.command);

                    // Remover el comando ya ejecutado
                    iterator.remove();
                }
            }

            // Si no quedan comandos pendientes, remover este jugador de la lista
            return deathData.pendingCommands.isEmpty();
        });
    }

    /**
     * Ejecuta un comando en el servidor
     */
    private static void executeCommand(MinecraftServer server, String command) {
        try {
            ServerCommandSource source = server.getCommandSource();
            server.getCommandManager().executeWithPrefix(source, command);
            System.out.println("[SquidUtils] Comando ejecutado: " + command);
        } catch (Exception e) {
            System.err.println("[SquidUtils] Error al ejecutar comando '" + command + "': " + e.getMessage());
        }
    }

    /**
     * Cancela todos los comandos pendientes de un jugador
     */
    public static void cancelPlayerCommands(UUID playerUuid) {
        PlayerDeathData removed = activeDeaths.remove(playerUuid);
        if (removed != null) {
            System.out.println("[SquidUtils] Comandos cancelados para jugador: " + removed.playerName);
        }
    }

    /**
     * Datos de muerte de un jugador
     */
    private static class PlayerDeathData {
        final String playerName;
        final List<PendingCommand> pendingCommands;
        int ticksElapsed;

        PlayerDeathData(String playerName, List<PendingCommand> pendingCommands) {
            this.playerName = playerName;
            this.pendingCommands = pendingCommands;
            this.ticksElapsed = 0;
        }
    }

    /**
     * Comando pendiente de ejecución
     */
    private static class PendingCommand {
        final int delayTicks;
        final String command;

        PendingCommand(int delayTicks, String command) {
            this.delayTicks = delayTicks;
            this.command = command;
        }
    }
}

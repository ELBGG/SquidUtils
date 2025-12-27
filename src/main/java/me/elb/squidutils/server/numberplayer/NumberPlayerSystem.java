package me.elb. squidutils.server.numberplayer;

import net.minecraft. scoreboard.Scoreboard;
import net.minecraft.scoreboard. ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard. ScoreboardDisplaySlot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network. ServerPlayerEntity;
import net. minecraft.text.Text;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sistema autónomo de números de jugadores usando scoreboard BELOW_NAME
 */
public class NumberPlayerSystem {

    private static boolean active = false;
    private static final Map<UUID, Integer> playerNumbers = new ConcurrentHashMap<>();
    private static final Set<UUID> exemptPlayers = Collections.synchronizedSet(new HashSet<>());
    private static final Queue<Integer> availableNumbers = new LinkedList<>();
    private static int maxNumber = 0;

    private static final String OBJECTIVE_NAME = "squidutils_number";
    private static ScoreboardObjective numberObjective = null;

    /**
     * Activa el sistema (auto-inicio sin executor)
     */
    public static void start(MinecraftServer server) {
        start(server, null);
    }

    /**
     * Activa el sistema
     */
    public static void start(MinecraftServer server, ServerPlayerEntity executor) {
        if (active) {
            if (executor != null) {
                executor.sendMessage(Text.literal("§c⚠ El sistema de números ya está activo"));
            }
            return;
        }

        active = true;

        // Crear objective del scoreboard
        createScoreboardObjective(server);

        // Marcar jugadores actuales como exentos
        for (ServerPlayerEntity player : server. getPlayerManager().getPlayerList()) {
            exemptPlayers. add(player.getUuid());
        }

        broadcast(server, "§a§l========================================");
        broadcast(server, "§2§l   ✓ SISTEMA DE NÚMEROS ACTIVADO ✓");
        broadcast(server, "§a§l========================================");
        broadcast(server, "§7Los nuevos jugadores recibirán un número");
        broadcast(server, "§7Jugadores actuales: §eEXENTOS");
        broadcast(server, "§a§l========================================");
    }

    /**
     * Desactiva el sistema
     */
    public static void stop(MinecraftServer server, ServerPlayerEntity executor) {
        if (!active) {
            executor.sendMessage(Text.literal("§c⚠ El sistema de números no está activo"));
            return;
        }

        active = false;

        // Remover objective del scoreboard
        removeScoreboardObjective(server);

        // Limpiar experiencia
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.setExperienceLevel(0);
            player.setExperiencePoints(0);
        }

        clear();

        broadcast(server, "§c§l========================================");
        broadcast(server, "§4§l   ⚠ SISTEMA DE NÚMEROS DESACTIVADO ⚠");
        broadcast(server, "§c§l========================================");
    }

    /**
     * Crea el objective del scoreboard para mostrar debajo del nombre
     */
    private static void createScoreboardObjective(MinecraftServer server) {
        Scoreboard scoreboard = server.getScoreboard();

        // Remover objective anterior si existe
        ScoreboardObjective existing = scoreboard.getNullableObjective(OBJECTIVE_NAME);
        if (existing != null) {
            scoreboard. removeObjective(existing);
        }

        // Crear nuevo objective con el criterio dummy
        numberObjective = scoreboard.addObjective(
                OBJECTIVE_NAME,
                ScoreboardCriterion. DUMMY,
                Text.literal("♫"), // Título que aparece (ícono)
                ScoreboardCriterion.RenderType.INTEGER,
                true,
                null
        );

        // Establecer que se muestre debajo del nombre usando ScoreboardDisplaySlot
        scoreboard.setObjectiveSlot(
                ScoreboardDisplaySlot.BELOW_NAME, // ✅ Correcto para 1.21+
                numberObjective
        );
    }

    /**
     * Remueve el objective del scoreboard
     */
    private static void removeScoreboardObjective(MinecraftServer server) {
        if (numberObjective != null) {
            Scoreboard scoreboard = server.getScoreboard();
            scoreboard.removeObjective(numberObjective);
            numberObjective = null;
        }
    }

    /**
     * Maneja cuando un jugador entra
     */
    public static void onPlayerJoin(ServerPlayerEntity player, MinecraftServer server) {
        if (!active) return;

        UUID uuid = player. getUuid();

        // Si es admin, no asignar
        if (isAdmin(player, server)) {
            return;
        }

        // Si está exento, no asignar
        if (exemptPlayers.contains(uuid)) {
            return;
        }

        // Si ya tiene número, restaurarlo
        if (playerNumbers.containsKey(uuid)) {
            int number = playerNumbers.get(uuid);
            applyNumberToPlayer(player, number, server);
            return;
        }

        // Asignar nuevo número
        int number = getNextAvailableNumber();
        playerNumbers.put(uuid, number);
        applyNumberToPlayer(player, number, server);
    }

    /**
     * Maneja cuando un jugador sale
     */
    public static void onPlayerLeave(ServerPlayerEntity player, MinecraftServer server) {
        if (!active) return;
        // Mantener el número para cuando vuelva
    }

    /**
     * Aplica el número al jugador en el scoreboard
     */
    private static void applyNumberToPlayer(ServerPlayerEntity player, int number, MinecraftServer server) {
        // Establecer nivel de experiencia (para el cliente local)
        player.setExperienceLevel(number);
        player.setExperiencePoints(0);

        // Establecer score en el objective (esto se muestra debajo del nombre)
        if (numberObjective != null) {
            Scoreboard scoreboard = server.getScoreboard();
            scoreboard.getOrCreateScore(player, numberObjective).setScore(number);
        }
    }

    /**
     * Obtiene el siguiente número disponible
     */
    private static int getNextAvailableNumber() {
        if (! availableNumbers.isEmpty()) {
            return availableNumbers.poll();
        }

        maxNumber++;
        return maxNumber;
    }

    /**
     * Verifica si un jugador es admin
     */
    private static boolean isAdmin(ServerPlayerEntity player, MinecraftServer server) {
        return server.getPlayerManager().isOperator(player.getGameProfile());
    }

    /**
     * Formatea el número con ceros
     */
    public static String formatNumber(int number) {
        if (number < 0) return "";
        return String.format("%03d", number);
    }

    /**
     * Obtiene el número de un jugador
     */
    public static int getPlayerNumber(UUID uuid) {
        return playerNumbers.getOrDefault(uuid, -1);
    }

    /**
     * Verifica si el sistema está activo
     */
    public static boolean isActive() {
        return active;
    }

    /**
     * Limpia todos los datos
     */
    private static void clear() {
        playerNumbers.clear();
        exemptPlayers.clear();
        availableNumbers.clear();
        maxNumber = 0;
        numberObjective = null;
    }

    /**
     * Broadcast a todos los jugadores
     */
    private static void broadcast(MinecraftServer server, String message) {
        for (ServerPlayerEntity player :  server.getPlayerManager().getPlayerList()) {
            player.sendMessage(Text.literal(message));
        }
    }

    /**
     * Obtiene estadísticas
     */
    public static String getStats() {
        return String.format("§7Estado:  %s§r | §7Jugadores con número: §f%d§r | §7Exentos: §f%d§r | §7Máximo:  §f%d",
                active ? "§aACTIVO" : "§cINACTIVO",
                playerNumbers. size(),
                exemptPlayers.size(),
                maxNumber);
    }

    /**
     * Remueve la exención de un jugador
     */
    public static void removeExemption(UUID uuid) {
        exemptPlayers.remove(uuid);
    }

    /**
     * Obtiene todos los números asignados
     */
    public static Map<UUID, Integer> getAllNumbers() {
        return new HashMap<>(playerNumbers);
    }

    /**
     * Cambia el título/ícono que aparece
     */
    public static void setDisplayTitle(MinecraftServer server, String title) {
        if (numberObjective != null) {
            numberObjective.setDisplayName(Text.literal(title));
        }
    }
}
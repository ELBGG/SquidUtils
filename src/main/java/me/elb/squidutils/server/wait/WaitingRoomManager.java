package me.elb.squidutils.server.wait;

import net.fabricmc.fabric.api.networking. v1.ServerPlayNetworking;
import net. minecraft.server.MinecraftServer;
import net.minecraft.server.network. ServerPlayerEntity;
import net. minecraft.text.Text;
import me.elb.squidutils. net.SyncWaitingRoomPayload;
import me.elb.squidutils.client.data.GameStage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WaitingRoomManager {
    private static final WaitingRoomManager INSTANCE = new WaitingRoomManager();

    private MinecraftServer server;
    private int maxPlayers = 10;
    private GameStage stage = GameStage.LOBBY;
    private final Set<UUID> playerUUIDs = ConcurrentHashMap.newKeySet();
    private boolean isActive = false;

    public static WaitingRoomManager getInstance() {
        return INSTANCE;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }


    public void startWaitingRoom(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.isActive = true;
        this. stage = GameStage.WAITING;
        this.playerUUIDs. clear();

        if (server == null) {
            System.err.println("[SquidUtils] ERROR: Server is null!  Cannot start waiting room.");
            System.err.println("[SquidUtils] Make sure the server has fully started before running this command.");
            return;
        }

        System.out. println("[SquidUtils] Server reference OK, starting waiting room...");

        // Añadir automáticamente a todos los jugadores sin OP
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (!isOperator(player)) {
                addPlayerSilently(player);
                System.out.println("[SquidUtils] Auto-added player:  " + player.getName().getString());
            } else {
                System.out. println("[SquidUtils] Skipped OP player: " + player.getName().getString());
            }
        }

        syncToAllPlayers();
        System.out.println("[SquidUtils] Waiting room started with " + playerUUIDs.size() + " players");
    }

    /**
     * Detiene la sala de espera y resetea todos los clientes
     */
    public void stopWaitingRoom() {
        this.isActive = false;
        this.stage = GameStage.LOBBY;

        if (server == null) return;

        // Enviar reset a todos los jugadores que estaban en la sala
        for (UUID uuid : new HashSet<>(playerUUIDs)) {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player != null) {
                sendResetToPlayer(player);
            }
        }

        playerUUIDs.clear();
        System.out.println("[SquidUtils] Waiting room stopped");
    }

    /**
     * Se llama automáticamente cuando un jugador se une al servidor
     */
    public void onPlayerJoin(ServerPlayerEntity player) {
        if (!isActive) return;
        if (isOperator(player)) return; // Los OPs no entran automáticamente

        if (playerUUIDs.size() >= maxPlayers) {
            player.sendMessage(Text.literal("§cThe waiting room is full! "), false);
            return;
        }

        addPlayerSilently(player);
        syncToAllPlayers();

        System.out.println("[SquidUtils] Player " + player.getName().getString() + " auto-joined waiting room");
    }

    /**
     * Se llama automáticamente cuando un jugador se desconecta
     */
    public void onPlayerLeave(ServerPlayerEntity player) {
        if (!playerUUIDs.contains(player. getUuid())) return;

        playerUUIDs.remove(player.getUuid());

        // Volver a WAITING si estaba en STARTING
        if (stage == GameStage.STARTING && playerUUIDs.size() < maxPlayers) {
            stage = GameStage.WAITING;
        }

        syncToAllPlayers();
        System.out.println("[SquidUtils] Player " + player.getName().getString() + " left waiting room");
    }

    /**
     * Añade un jugador sin mensaje (uso interno)
     */
    private void addPlayerSilently(ServerPlayerEntity player) {
        playerUUIDs.add(player. getUuid());

        // Si se llena, cambiar a STARTING
        if (playerUUIDs.size() >= maxPlayers) {
            stage = GameStage.STARTING;
        }
    }

    private void syncToAllPlayers() {
        if (!  isActive || server == null) {
            System.out.println("[SquidUtils] Sync skipped - isActive: " + isActive + ", server: " + (server != null));
            return;
        }

        List<String> playerNames = new ArrayList<>();
        for (UUID uuid : playerUUIDs) {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player != null) {
                playerNames.add(player. getName().getString());
            }
        }

        SyncWaitingRoomPayload payload = new SyncWaitingRoomPayload(
                playerUUIDs. size(),
                maxPlayers,
                playerNames,
                stage. name()
        );

        System.out.println("[SquidUtils] Syncing to " + playerUUIDs.size() + " players - Stage: " + stage.name());

        // Enviar solo a jugadores en la sala
        for (UUID uuid : playerUUIDs) {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player != null) {
                ServerPlayNetworking.send(player, payload);
                System.out.println("[SquidUtils] Packet sent to:  " + player.getName().getString());
            }
        }
    }

    /**
     * Envía un reset al cliente (oculta el HUD)
     */
    private void sendResetToPlayer(ServerPlayerEntity player) {
        SyncWaitingRoomPayload payload = new SyncWaitingRoomPayload(
                0,
                maxPlayers,
                new ArrayList<>(),
                GameStage.LOBBY.name()
        );
        ServerPlayNetworking.send(player, payload);
    }

    /**
     * Verifica si un jugador tiene permisos de operador
     */
    private boolean isOperator(ServerPlayerEntity player) {
        return player. hasPermissionLevel(2); // Level 2 = OP
    }

    // Getters
    public boolean isActive() {
        return isActive;
    }

    public int getCurrentPlayers() {
        return playerUUIDs.size();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameStage getStage() {
        return stage;
    }

    public void setStage(GameStage stage) {
        this.stage = stage;
        syncToAllPlayers();
    }
}
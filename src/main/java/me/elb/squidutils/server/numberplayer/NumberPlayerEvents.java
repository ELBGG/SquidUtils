package me.elb.squidutils.server.numberplayer;

import me.elb.squidutils.server. wait.WaitingRoomManager;
import net.fabricmc. fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class NumberPlayerEvents {

    public static void register() {
        System.out.println("[SquidUtils] Registering player connection events...");

        // Evento de join
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();

            // Sistema de números de jugador
            NumberPlayerSystem.onPlayerJoin(player, server);

            // Sistema de waiting room
            server.execute(() -> {
                System.out.println("[SquidUtils] Player joining:  " + player.getName().getString());
                WaitingRoomManager.getInstance().onPlayerJoin(player);
            });
        });

        // Evento de disconnect
        ServerPlayConnectionEvents. DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();

            // Sistema de números de jugador
            NumberPlayerSystem. onPlayerLeave(player, server);

            // Sistema de waiting room
            server.execute(() -> {
                System.out. println("[SquidUtils] Player leaving: " + player.getName().getString());
                WaitingRoomManager.getInstance().onPlayerLeave(player);
            });
        });

        System.out.println("[SquidUtils] Player connection events registered successfully");
    }
}
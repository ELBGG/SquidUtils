package me. elb.squidutils.server.numberplayer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc. fabric.api.networking.v1.ServerPlayConnectionEvents;
import me.elb.squidutils. command.SquidUtilsCommand;

public class NumberPlayerEvents {

    public static void register() {
        // Registrar comando
        CommandRegistrationCallback.EVENT.register(SquidUtilsCommand:: register);

        // Evento de join
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NumberPlayerSystem.onPlayerJoin(handler.player, server);
        });

        // Evento de disconnect
        ServerPlayConnectionEvents. DISCONNECT.register((handler, server) -> {
            NumberPlayerSystem. onPlayerLeave(handler.player, server);
        });
    }
}
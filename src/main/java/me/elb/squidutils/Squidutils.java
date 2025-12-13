package me.elb.squidutils;

import me.elb.squidutils.api. BlurEffectAPI;
import me.elb.squidutils.command.SquidUtilsCommand;
import me.elb.squidutils.command. WaitingRoomCommand;
import me.elb.squidutils.net.SquidUtilsNetwork;
import me. elb.squidutils.server.numberplayer.NumberPlayerEvents;
import me.elb. squidutils.server.playereliminated.SoulDepartureSystem;
import me.elb.squidutils.server.wait.WaitingRoomManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event. lifecycle.v1.ServerLifecycleEvents;
import net. fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Squidutils implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("[SquidUtils] Initializing server...");

        // ✅ Registrar comandos
        CommandRegistrationCallback.EVENT.register(SquidUtilsCommand::register);
        CommandRegistrationCallback.EVENT.register(WaitingRoomCommand::register);
        System.out.println("[SquidUtils] Commands registered");

        // ✅ Registrar eventos de jugadores (incluyendo waiting room)
        NumberPlayerEvents.register();
        System.out.println("[SquidUtils] Player events registered");

        // ✅ Registrar paquetes de red
        SquidUtilsNetwork.registerPackets();
        System.out.println("[SquidUtils] Network packets registered");

        // ✅ CRÍTICO: Registrar el servidor en WaitingRoomManager cuando esté iniciando
        ServerLifecycleEvents.SERVER_STARTING. register(server -> {
            WaitingRoomManager. getInstance().setServer(server);
            System.out.println("[SquidUtils] Server reference set in WaitingRoomManager");
        });

        ServerLifecycleEvents.SERVER_STARTED. register(server -> {
            System.out.println("[SquidUtils] Server fully started and ready");
        });

        // ✅ Tick events para otros sistemas
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            BlurEffectAPI.tick(server);
            SoulDepartureSystem.tick(server);
        });

        System.out. println("[SquidUtils] Server initialized successfully!");
    }
}
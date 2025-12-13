package me.elb.squidutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.elb.squidutils.net.LimitedInventoryPacket;
import me.elb.squidutils.server.limitedinventory.LimitedInventorySystem;
import me.elb.squidutils. server.numberplayer.NumberPlayerSystem;
import me.elb.squidutils.server.playereliminated.CustomDeathMessageSystem;
import net.minecraft. command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command. ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SquidUtilsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager. RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("squidutils")
                        .requires(source -> source.hasPermissionLevel(2))
                        . then(CommandManager.literal("numberplayer")
                                .then(CommandManager.literal("start")
                                        .executes(SquidUtilsCommand::startNumberPlayer)
                                )
                                .then(CommandManager.literal("stop")
                                        .executes(SquidUtilsCommand::stopNumberPlayer)
                                )
                                .then(CommandManager.literal("stats")
                                        .executes(SquidUtilsCommand::statsNumberPlayer)
                                )
                        )
                        .then(CommandManager.literal("deathmessage")
                                .then(CommandManager.literal("start")
                                        .executes(SquidUtilsCommand:: startDeathMessage)
                                )
                                .then(CommandManager. literal("stop")
                                        .executes(SquidUtilsCommand::stopDeathMessage)
                                )
                        )
                        .then(CommandManager.literal("limitedinventory")
                                .then(CommandManager.literal("start")
                                        .executes(SquidUtilsCommand::startLimitedInventory)
                                )
                                .then(CommandManager.literal("stop")
                                        .executes(SquidUtilsCommand::stopLimitedInventory)
                                )
                        )
        );
    }

    private static int startNumberPlayer(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var server = source.getServer();
        var player = source.getPlayer();

        if (player != null) {
            NumberPlayerSystem.start(server, player);
        } else {
            source.sendFeedback(() -> Text.literal("§cEste comando debe ser ejecutado por un jugador"), false);
        }

        return 1;
    }

    private static int stopNumberPlayer(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var server = source.getServer();
        var player = source.getPlayer();

        if (player != null) {
            NumberPlayerSystem.stop(server, player);
        } else {
            source.sendFeedback(() -> Text.literal("§cEste comando debe ser ejecutado por un jugador"), false);
        }

        return 1;
    }

    private static int statsNumberPlayer(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        String stats = NumberPlayerSystem.getStats();

        source.sendFeedback(() -> Text.literal("§b§l[SquidUtils] §r" + stats), false);

        return 1;
    }

    private static int startDeathMessage(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();

        CustomDeathMessageSystem.activate();
        source.sendFeedback(() -> Text.literal("§a✓ Mensajes de muerte personalizados activados"), false);

        return 1;
    }

    private static int stopDeathMessage(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();

        CustomDeathMessageSystem.deactivate();
        source.sendFeedback(() -> Text.literal("§c✗ Mensajes de muerte personalizados desactivados"), false);

        return 1;
    }



    private static int startLimitedInventory(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var server = source.getServer();

        LimitedInventorySystem.activate();

        // Activar para todos los jugadores online
        for (ServerPlayerEntity player : server. getPlayerManager().getPlayerList()) {
            LimitedInventorySystem.enableForPlayer(player. getUuid());
            LimitedInventoryPacket.send(player, true);
        }

        source.sendFeedback(() -> Text.literal("§a✓ Inventario limitado activado (solo hotbar + armadura)"), true);
        return 1;
    }

    private static int stopLimitedInventory(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var server = source.getServer();

        LimitedInventorySystem.deactivate();

        // Desactivar para todos los jugadores
        for (ServerPlayerEntity player :  server.getPlayerManager().getPlayerList()) {
            LimitedInventoryPacket.send(player, false);
        }

        source.sendFeedback(() -> Text.literal("§c✗ Inventario limitado desactivado"), true);
        return 1;
    }

}
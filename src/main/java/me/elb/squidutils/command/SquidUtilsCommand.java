package me.elb.squidutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.elb.squidutils. server.numberplayer.NumberPlayerSystem;
import me.elb.squidutils.server.playereliminated.CustomDeathMessageSystem;
import net.minecraft. command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command. ServerCommandSource;
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

}
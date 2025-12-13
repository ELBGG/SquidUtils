package me.elb.squidutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier. context.CommandContext;
import me.elb.squidutils.server.wait.WaitingRoomManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft. server.command.ServerCommandSource;
import net.minecraft.text. Text;

public class WaitingRoomCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        dispatcher. register(CommandManager.literal("waitingroom")
                .requires(source -> source.hasPermissionLevel(2)) // Solo OPs

                // /waitingroom start <maxPlayers>
                .then(CommandManager.literal("start")
                        .then(CommandManager.argument("maxPlayers", IntegerArgumentType.integer(1, 100))
                                .executes(WaitingRoomCommand::startWaitingRoom)
                        )
                )

                // /waitingroom stop
                .then(CommandManager. literal("stop")
                        .executes(WaitingRoomCommand::stopWaitingRoom)
                )

                // /waitingroom status
                .then(CommandManager. literal("status")
                        .executes(WaitingRoomCommand::showStatus)
                )
        );
    }

    private static int startWaitingRoom(CommandContext<ServerCommandSource> context) {
        int maxPlayers = IntegerArgumentType.getInteger(context, "maxPlayers");
        WaitingRoomManager manager = WaitingRoomManager.getInstance();

        if (manager.isActive()) {
            context.getSource().sendError(Text.literal("§cWaiting room is already active!"));
            return 0;
        }

        manager.startWaitingRoom(maxPlayers);

        context.getSource().sendFeedback(
                () -> Text.literal("§a✓ Waiting room started!")
                        .append(Text.literal("\n§7Max players: §f" + maxPlayers))
                        .append(Text.literal("\n§7Current players: §f" + manager. getCurrentPlayers())),
                true
        );
        return 1;
    }

    private static int stopWaitingRoom(CommandContext<ServerCommandSource> context) {
        WaitingRoomManager manager = WaitingRoomManager.getInstance();

        if (!manager.isActive()) {
            context.getSource().sendError(Text.literal("§cNo waiting room is active!"));
            return 0;
        }

        manager.stopWaitingRoom();

        context.getSource().sendFeedback(
                () -> Text.literal("§c✓ Waiting room stopped! "),
                true
        );
        return 1;
    }

    private static int showStatus(CommandContext<ServerCommandSource> context) {
        WaitingRoomManager manager = WaitingRoomManager.getInstance();

        if (!manager.isActive()) {
            context.getSource().sendFeedback(
                    () -> Text.literal("§7No waiting room is active"),
                    false
            );
            return 1;
        }

        context.getSource().sendFeedback(
                () -> Text.literal("§e⬛ Waiting Room Status")
                        .append(Text.literal("\n§7Players: §f" + manager.getCurrentPlayers() + "/" + manager.getMaxPlayers()))
                        .append(Text.literal("\n§7Stage: §f" + manager.getStage().name())),
                false
        );
        return 1;
    }
}
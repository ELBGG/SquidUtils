package me.elb.squidutils;

import me.elb.squidutils.api.BlurEffectAPI;
import me.elb.squidutils.net.SquidUtilsNetwork;
import me.elb.squidutils.server.numberplayer.NumberPlayerEvents;
import me.elb.squidutils.server.playereliminated.SoulDepartureSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Squidutils implements ModInitializer {

    @Override
    public void onInitialize() {

        NumberPlayerEvents.register();
        SquidUtilsNetwork.registerPackets();

        ServerTickEvents. END_SERVER_TICK.register(server -> {
            BlurEffectAPI.tick(server);
            SoulDepartureSystem.tick(server); // Tick del sistema de almas
        });
    }
}

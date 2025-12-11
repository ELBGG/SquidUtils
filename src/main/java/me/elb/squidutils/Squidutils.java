package me.elb.squidutils;

import me.elb.squidutils.api.BlurEffectAPI;
import me.elb.squidutils.net.SquidUtilsNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Squidutils implements ModInitializer {

    @Override
    public void onInitialize() {

        SquidUtilsNetwork.registerPackets();

        ServerTickEvents. END_SERVER_TICK.register(BlurEffectAPI::tick);
    }
}

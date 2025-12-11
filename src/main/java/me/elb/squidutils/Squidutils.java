package me.elb.squidutils;

import me.elb.squidutils.net.SquidUtilsNetwork;
import net.fabricmc.api.ModInitializer;

public class Squidutils implements ModInitializer {

    @Override
    public void onInitialize() {

        SquidUtilsNetwork.registerPackets();
    }
}

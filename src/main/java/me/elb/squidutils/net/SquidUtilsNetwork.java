package me.elb.squidutils.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class SquidUtilsNetwork {

    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(BlurEffectPacket.ID, BlurEffectPacket. CODEC);
        PayloadTypeRegistry. playS2C().register(PlayerTitlePacket.ID, PlayerTitlePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(DeathFadePacket.ID, DeathFadePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(SoulStatePacket.ID, SoulStatePacket.CODEC);
    }

    public static void registerClientReceivers() {
        BlurEffectPacket.registerClientReceiver();
        PlayerTitlePacket. registerClientReceiver();
        DeathFadePacket.registerClientReceiver();
        SoulStatePacket.registerClientReceiver();
    }
}
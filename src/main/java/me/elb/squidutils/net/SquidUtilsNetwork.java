package me.elb.squidutils.net;

import net.fabricmc.fabric.api. networking.v1.PayloadTypeRegistry;

public class SquidUtilsNetwork {
    
    public static void registerPackets() {
        // Registrar el packet (común para cliente y servidor)
        PayloadTypeRegistry.playS2C().register(BlurEffectPacket. ID, BlurEffectPacket. CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerTitlePacket.ID, PlayerTitlePacket.CODEC);
    }

    public static void registerClientReceivers() {
        // Registrar el receptor del cliente
        BlurEffectPacket.registerClientReceiver();
        PlayerTitlePacket.registerClientReceiver();
    }
}
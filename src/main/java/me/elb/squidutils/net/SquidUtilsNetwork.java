package me.elb.squidutils.net;

import net.fabricmc.fabric.api. networking.v1.PayloadTypeRegistry;

public class SquidUtilsNetwork {

    public static void registerPackets() {
        PayloadTypeRegistry. playS2C().register(BlurEffectPacket.ID, BlurEffectPacket. CODEC);
        PayloadTypeRegistry. playS2C().register(PlayerTitlePacket.ID, PlayerTitlePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(DeathFadePacket.ID, DeathFadePacket.CODEC);
        PayloadTypeRegistry. playS2C().register(SoulStatePacket.ID, SoulStatePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncWaitingRoomPayload.ID, SyncWaitingRoomPayload. CODEC);
        PayloadTypeRegistry.playS2C().register(WaitingHudConfigPacket.ID, WaitingHudConfigPacket.CODEC);
    }

    public static void registerClientReceivers() {
        BlurEffectPacket.registerClientReceiver();
        PlayerTitlePacket. registerClientReceiver();
        DeathFadePacket.registerClientReceiver();
        SoulStatePacket.registerClientReceiver();
        SyncWaitingRoomPayload.registerClientReceiver();
        WaitingHudConfigPacket.registerClientReceiver();
    }
}
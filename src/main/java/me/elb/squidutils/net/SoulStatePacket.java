package me. elb.squidutils.net;

import me.elb.squidutils.client.data.ClientSoulState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net. fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network. RegistryByteBuf;
import net. minecraft.network.codec.PacketCodec;
import net.minecraft. network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util. Identifier;

public record SoulStatePacket(
        boolean active
) implements CustomPayload {

    public static final CustomPayload.Id<SoulStatePacket> ID =
            new CustomPayload.Id<>(Identifier.of("squidutils", "soul_state"));

    public static final PacketCodec<RegistryByteBuf, SoulStatePacket> CODEC =
            PacketCodec. of(SoulStatePacket::encode, SoulStatePacket::decode);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void encode(SoulStatePacket packet, RegistryByteBuf buf) {
        buf.writeBoolean(packet.active);
    }

    private static SoulStatePacket decode(RegistryByteBuf buf) {
        return new SoulStatePacket(buf. readBoolean());
    }

    public static void send(ServerPlayerEntity player, boolean active) {
        ServerPlayNetworking.send(player, new SoulStatePacket(active));
    }

    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (packet, context) -> context.client().execute(() -> ClientSoulState.setSoulState(packet.active)));
    }
}
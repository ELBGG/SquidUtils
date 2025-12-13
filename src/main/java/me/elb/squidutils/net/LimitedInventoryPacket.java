package me.elb.squidutils.net;

import me.elb. squidutils.client.data.ClientLimitedInventory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network. RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet. CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util. Identifier;

public record LimitedInventoryPacket(
        boolean enabled
) implements CustomPayload {

    public static final CustomPayload.Id<LimitedInventoryPacket> ID =
            new CustomPayload.Id<>(Identifier.of("squidutils", "limited_inventory"));

    public static final PacketCodec<RegistryByteBuf, LimitedInventoryPacket> CODEC =
            PacketCodec. of(LimitedInventoryPacket::encode, LimitedInventoryPacket::decode);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void encode(LimitedInventoryPacket packet, RegistryByteBuf buf) {
        buf.writeBoolean(packet.enabled);
    }

    private static LimitedInventoryPacket decode(RegistryByteBuf buf) {
        return new LimitedInventoryPacket(buf.readBoolean());
    }

    public static void send(ServerPlayerEntity player, boolean enabled) {
        ServerPlayNetworking.send(player, new LimitedInventoryPacket(enabled));
    }

    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (packet, context) -> {
            context.client().execute(() -> {
                ClientLimitedInventory.setEnabled(packet.enabled);
            });
        });
    }
}
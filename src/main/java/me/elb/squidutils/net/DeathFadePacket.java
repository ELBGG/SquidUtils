package me.elb.squidutils.net;

import me.elb.squidutils.client.shader.ClientDeathFade;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net. fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network. RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet. CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util. Identifier;

public record DeathFadePacket(
        boolean activate
) implements CustomPayload {

    public static final CustomPayload.Id<DeathFadePacket> ID =
            new CustomPayload.Id<>(Identifier.of("squidutils", "death_fade"));

    public static final PacketCodec<RegistryByteBuf, DeathFadePacket> CODEC =
            PacketCodec. of(DeathFadePacket:: encode, DeathFadePacket:: decode);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static void encode(DeathFadePacket packet, RegistryByteBuf buf) {
        buf.writeBoolean(packet.activate);
    }

    private static DeathFadePacket decode(RegistryByteBuf buf) {
        return new DeathFadePacket(buf.readBoolean());
    }

    /**
     * Enviar packet al cliente
     */
    public static void send(ServerPlayerEntity player, boolean activate) {
        ServerPlayNetworking.send(player, new DeathFadePacket(activate));
    }

    /**
     * Registrar receptor del cliente
     */
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (packet, context) -> context.client().execute(() -> {
            if (packet.activate) {
                ClientDeathFade.activate();
            } else {
                ClientDeathFade.deactivate();
            }
        }));
    }
}
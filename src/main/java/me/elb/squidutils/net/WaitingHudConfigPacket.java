package me.elb.squidutils.net;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import me.elb.squidutils.client.data.ClientWaitingHudConfig;

/**
 * Packet para sincronizar configuración del WaitingHud al cliente
 */
public record WaitingHudConfigPacket(
    int mainTextColor,
    int mainTextShadowColor,
    int counterColor,
    int counterShadowColor,
    int startingTextColor,
    float mainTextScale,
    float counterScale,
    float mainTextY,
    int counterOffsetY
) implements CustomPayload {

    public static final CustomPayload.Id<WaitingHudConfigPacket> ID =
        new CustomPayload.Id<>(Identifier.of("squidutils", "waiting_hud_config"));

    public static final PacketCodec<RegistryByteBuf, WaitingHudConfigPacket> CODEC = new PacketCodec<>() {
        @Override
        public WaitingHudConfigPacket decode(RegistryByteBuf buf) {
            return new WaitingHudConfigPacket(
                buf.readInt(),   // mainTextColor
                buf.readInt(),   // mainTextShadowColor
                buf.readInt(),   // counterColor
                buf.readInt(),   // counterShadowColor
                buf.readInt(),   // startingTextColor
                buf.readFloat(), // mainTextScale
                buf.readFloat(), // counterScale
                buf.readFloat(), // mainTextY
                buf.readInt()    // counterOffsetY
            );
        }

        @Override
        public void encode(RegistryByteBuf buf, WaitingHudConfigPacket packet) {
            buf.writeInt(packet.mainTextColor);
            buf.writeInt(packet.mainTextShadowColor);
            buf.writeInt(packet.counterColor);
            buf.writeInt(packet.counterShadowColor);
            buf.writeInt(packet.startingTextColor);
            buf.writeFloat(packet.mainTextScale);
            buf.writeFloat(packet.counterScale);
            buf.writeFloat(packet.mainTextY);
            buf.writeInt(packet.counterOffsetY);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    /**
     * Envía la configuración a un jugador
     */
    public static void send(ServerPlayerEntity player, WaitingHudConfigPacket packet) {
        ServerPlayNetworking.send(player, packet);
    }

    /**
     * Registra el receptor en el cliente
     */
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (payload, context) -> {
            context.client().execute(() -> {
                // Aplicar configuración en el cliente
                ClientWaitingHudConfig.updateConfig(
                    payload.mainTextColor,
                    payload.mainTextShadowColor,
                    payload.counterColor,
                    payload.counterShadowColor,
                    payload.startingTextColor,
                    payload.mainTextScale,
                    payload.counterScale,
                    payload.mainTextY,
                    payload.counterOffsetY
                );
            });
        });
    }
}

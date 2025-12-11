package me.elb. squidutils.net;

import me.elb.squidutils.client.data.ClientPlayerColor;
import net.fabricmc.fabric. api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric. api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net. minecraft.network.packet.CustomPayload;
import net.minecraft. server.network.ServerPlayerEntity;
import net.minecraft.util. Identifier;

public record BlurEffectPacket(
        boolean enabled,
        float borderWidth,
        float blurIntensity,
        float colorR,
        float colorG,
        float colorB
) implements CustomPayload {

    public static final CustomPayload.Id<BlurEffectPacket> ID =
            new CustomPayload.Id<>(Identifier.of("squidutils", "blur_effect"));

    public static final PacketCodec<RegistryByteBuf, BlurEffectPacket> CODEC =
            PacketCodec.of(BlurEffectPacket::encode, BlurEffectPacket:: decode);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    // El encoder recibe (valor, buffer) - primero el packet, luego el buffer
    private static void encode(BlurEffectPacket packet, RegistryByteBuf buf) {
        buf.writeBoolean(packet.enabled);
        buf.writeFloat(packet.borderWidth);
        buf.writeFloat(packet.blurIntensity);
        buf.writeFloat(packet.colorR);
        buf.writeFloat(packet.colorG);
        buf.writeFloat(packet.colorB);
    }

    // El decoder recibe (buffer) y retorna el packet
    private static BlurEffectPacket decode(RegistryByteBuf buf) {
        return new BlurEffectPacket(
                buf. readBoolean(),
                buf. readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    // Enviar desde el servidor
    public static void send(ServerPlayerEntity player, boolean enabled, float borderWidth,
                            float blurIntensity, float r, float g, float b) {
        ServerPlayNetworking.send(player, new BlurEffectPacket(enabled, borderWidth, blurIntensity, r, g, b));
    }

    // Registrar receptor del cliente
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (packet, context) -> context.client().execute(() -> ClientPlayerColor.setState(
                packet. enabled,
                packet.borderWidth,
                packet.blurIntensity,
                packet.colorR,
                packet.colorG,
                packet.colorB
        )));
    }
}
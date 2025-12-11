package me.elb.squidutils.net;

import me.elb.squidutils.client.data.ClientPlayerTitle;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api. networking.v1.ServerPlayNetworking;
import net.minecraft.network. RegistryByteBuf;
import net. minecraft.network.codec.PacketCodec;
import net.minecraft. network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record PlayerTitlePacket(
        boolean enabled,
        String title,
        String subtitle,
        int titleColor,
        float offsetX,
        float offsetY,
        float titleScale,
        float subtitleScale,
        long displayDuration,
        long fadeOutDuration,
        boolean autoFade
) implements CustomPayload {

    public static final CustomPayload. Id<PlayerTitlePacket> ID =
            new CustomPayload.Id<>(Identifier.of("squidutils", "player_title"));

    public static final PacketCodec<RegistryByteBuf, PlayerTitlePacket> CODEC =
            PacketCodec. of(PlayerTitlePacket:: encode, PlayerTitlePacket::decode);

    @Override
    public Id<?  extends CustomPayload> getId() {
        return ID;
    }

    private static void encode(PlayerTitlePacket packet, RegistryByteBuf buf) {
        buf.writeBoolean(packet.enabled);
        buf.writeString(packet.title);
        buf.writeString(packet.subtitle);
        buf.writeInt(packet.titleColor);
        buf.writeFloat(packet.offsetX);
        buf.writeFloat(packet.offsetY);
        buf.writeFloat(packet.titleScale);
        buf.writeFloat(packet.subtitleScale);
        buf.writeLong(packet.displayDuration);
        buf.writeLong(packet.fadeOutDuration);
        buf.writeBoolean(packet.autoFade);
    }

    private static PlayerTitlePacket decode(RegistryByteBuf buf) {
        return new PlayerTitlePacket(
                buf.readBoolean(),
                buf.readString(),
                buf.readString(),
                buf.readInt(),
                buf. readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readLong(),
                buf.readLong(),
                buf.readBoolean()
        );
    }

    public static void send(ServerPlayerEntity player, boolean enabled, String title, String subtitle,
                            int titleColor, float offsetX, float offsetY, float titleScale, float subtitleScale,
                            long displayDuration, long fadeOutDuration, boolean autoFade) {
        ServerPlayNetworking.send(player, new PlayerTitlePacket(enabled, title, subtitle, titleColor,
                offsetX, offsetY, titleScale, subtitleScale, displayDuration, fadeOutDuration, autoFade));
    }

    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (packet, context) -> context.client().execute(() -> ClientPlayerTitle.setState(
                packet.enabled,
                packet.title,
                packet.subtitle,
                packet.titleColor,
                packet.offsetX,
                packet.offsetY,
                packet.titleScale,
                packet.subtitleScale,
                packet.displayDuration,
                packet.fadeOutDuration,
                packet.autoFade
        )));
    }
}
package me.elb.squidutils.net;

import me.elb.squidutils.client.screens.WaitingHud;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net. minecraft.network. RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet. CustomPayload;
import net. minecraft.util.Identifier;
import me.elb.squidutils.client.data.GameStage;

import java.util.List;

public record SyncWaitingRoomPayload(
        int currentPlayers,
        int maxPlayers,
        List<String> playersWaiting,
        String stage
) implements CustomPayload {

    public static final Identifier SYNC_WAITING_ROOM_ID =
            Identifier. of("squidutils", "sync_waiting_room");

    public static final Id<SyncWaitingRoomPayload> ID =
            new Id<>(SYNC_WAITING_ROOM_ID);

    public static final PacketCodec<RegistryByteBuf, SyncWaitingRoomPayload> CODEC =
            new PacketCodec<>() {
                @Override
                public SyncWaitingRoomPayload decode(RegistryByteBuf buf) {
                    return read(buf);
                }

                @Override
                public void encode(RegistryByteBuf buf, SyncWaitingRoomPayload payload) {
                    write(buf, payload);
                }
            };

    public static void write(RegistryByteBuf buf, SyncWaitingRoomPayload payload) {
        buf.writeInt(payload.currentPlayers);
        buf.writeInt(payload. maxPlayers);
        buf.writeInt(payload.playersWaiting.size());
        for (String player : payload.playersWaiting) {
            buf.writeString(player);
        }
        buf.writeString(payload.stage);
    }

    public static SyncWaitingRoomPayload read(RegistryByteBuf buf) {
        int current = buf.readInt();
        int max = buf.readInt();
        int size = buf.readInt();
        List<String> players = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            players.add(buf.readString());
        }
        String stage = buf.readString();
        return new SyncWaitingRoomPayload(current, max, players, stage);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    // ✅ MÉTODO NUEVO: Registrar el receptor en el cliente
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(
                SyncWaitingRoomPayload.ID,
                (payload, context) -> {
                    // Ejecutar en el hilo principal del cliente
                    context.client().execute(() -> {
                        try {
                            GameStage stage = GameStage.valueOf(payload.stage());

                            // Actualizar el HUD con los datos recibidos
                            WaitingHud. INSTANCE.updateData(
                                    payload.currentPlayers(),
                                    payload.maxPlayers(),
                                    payload.playersWaiting(),
                                    stage
                            );

                            System.out.println("[SquidUtils] Waiting room updated:  " +
                                    payload. currentPlayers() + "/" + payload.maxPlayers());
                        } catch (IllegalArgumentException e) {
                            System.err.println("[SquidUtils] Invalid game stage: " + payload.stage());
                        }
                    });
                }
        );
    }
}
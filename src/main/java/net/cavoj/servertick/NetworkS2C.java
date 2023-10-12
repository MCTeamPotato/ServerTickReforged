package net.cavoj.servertick;

import io.netty.buffer.Unpooled;
import net.cavoj.servertick.extensions.SerializableMetricsData;
import net.fabricmc.networking.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class NetworkS2C {
    public static final Identifier PACKET_FULL_METRICS = new Identifier("servertick", "metrics/full");
    public static final Identifier PACKET_LAST_SAMPLE = new Identifier("servertick", "metrics/sample");

    public static void sendFullMetrics(@NotNull SerializableMetricsData metrics, ServerPlayerEntity player) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        metrics.st$serialize(data);
        ServerPlayNetworking.send(player, PACKET_FULL_METRICS, data);
    }

    public static void sendLastSample(long sample, ServerPlayerEntity player) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeLong(sample);
        ServerPlayNetworking.send(player, PACKET_LAST_SAMPLE, data);
    }
}

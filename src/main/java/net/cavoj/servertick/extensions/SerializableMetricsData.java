package net.cavoj.servertick.extensions;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public interface SerializableMetricsData {
    void st$deserialize(ByteBuf data);
    void st$serialize(PacketByteBuf data);
}

package net.cavoj.servertick.mixin;

import io.netty.buffer.ByteBuf;
import net.cavoj.servertick.extensions.LastSampleMetricsData;
import net.cavoj.servertick.extensions.SerializableMetricsData;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.MetricsData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MetricsData.class)
public abstract class MetricsDataMixin implements SerializableMetricsData, LastSampleMetricsData {

    @Unique
    private long st$lastSample;

    @Shadow @Final private long[] samples;

    @Shadow private int writeIndex;

    @Shadow private int sampleCount;

    @Shadow private int startIndex;

    @Override
    public void st$deserialize(@NotNull ByteBuf data) {
        this.writeIndex = data.readInt();
        this.sampleCount = data.readInt();
        this.startIndex = data.readInt();
        for (int i = 0; i < this.samples.length; i++)
            this.samples[i] = data.readLong();
    }

    @Override
    public void st$serialize(@NotNull PacketByteBuf data) {
        data.writeInt(this.writeIndex);
        data.writeInt(this.sampleCount);
        data.writeInt(this.startIndex);
        for (long sample : this.samples) data.writeLong(sample);
    }

    @Inject(method = "pushSample", at = @At("HEAD"))
    public void pushSample(long sample, CallbackInfo ci) {
        this.st$lastSample = sample;
    }

    @Override
    public long getSt$lastSample() {
        return this.st$lastSample;
    }
}

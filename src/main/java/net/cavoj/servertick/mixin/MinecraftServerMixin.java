package net.cavoj.servertick.mixin;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.cavoj.servertick.NetworkS2C;
import net.cavoj.servertick.extensions.LastSampleMetricsData;
import net.cavoj.servertick.extensions.MinecraftServerWithST;
import net.cavoj.servertick.extensions.SerializableMetricsData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.MetricsData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerWithST {
    @Shadow @Final private MetricsData metricsData;
    @Unique
    private final Set<ServerPlayerEntity> st$listeners = new ObjectOpenHashSet<>();

    @Override
    public void st$registerListener(ServerPlayerEntity player) {
        this.st$listeners.add(player);
        NetworkS2C.sendFullMetrics((SerializableMetricsData) this.metricsData, player);
    }

    @Override
    public void st$removeListener(ServerPlayerEntity player) {
        this.st$listeners.remove(player);
    }

    @Override
    public void st$tick() {
        long sample = ((LastSampleMetricsData)this.metricsData).getSt$lastSample();
        for (ServerPlayerEntity player : this.st$listeners) {
            NetworkS2C.sendLastSample(sample, player);
        }
    }
}

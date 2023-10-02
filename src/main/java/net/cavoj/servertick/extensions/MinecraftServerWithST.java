package net.cavoj.servertick.extensions;

import net.minecraft.server.network.ServerPlayerEntity;

public interface MinecraftServerWithST {
    void st$registerListener(ServerPlayerEntity player);
    void st$removeListener(ServerPlayerEntity player);
    void st$tick();
}

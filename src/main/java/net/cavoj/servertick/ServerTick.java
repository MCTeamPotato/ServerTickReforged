package net.cavoj.servertick;

import net.cavoj.servertick.extensions.MinecraftServerWithST;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

@Mod("servertick")
public class ServerTick {
    private Config config;

    public ServerTick() {
        if (FMLLoader.getDist().isClient()) ServerTickClient.getInstance().clientInit();
        ServerPlayNetworking.registerGlobalReceiver(NetworkC2S.PACKET_ENABLED, this::processTogglePacket);
        ServerTickEvents.END_SERVER_TICK.register((minecraftServer -> ((MinecraftServerWithST)minecraftServer).st$tick()));
        if (FMLLoader.getDist().isDedicatedServer()) this.config = new Config(FMLLoader.getGamePath().resolve("config").resolve("servertick.toml"));
    }

    private boolean checkPlayerPrivilege(@NotNull PlayerEntity player) {
        return (player.getServer() != null && !player.getServer().isDedicated()) ||
               (this.config != null && !this.config.requireOP) ||
               player.hasPermissionLevel(4);
    }

    private void processTogglePacket(@NotNull MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, @NotNull PacketByteBuf buf, PacketSender sender) {
        boolean state = buf.readBoolean();
        server.execute(() -> {
            MinecraftServerWithST serverST = (MinecraftServerWithST)server;
            if (state) {
                if (checkPlayerPrivilege(player)) {
                    serverST.st$registerListener(player);
                }
            } else {
                serverST.st$removeListener(player);
            }
        });
    }
}

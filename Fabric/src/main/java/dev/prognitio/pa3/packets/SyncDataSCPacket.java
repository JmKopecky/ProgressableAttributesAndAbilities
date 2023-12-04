package dev.prognitio.pa3.packets;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public class SyncDataSCPacket {
    public static void receive(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            CompoundTag data = buf.readAnySizeNbt();
            CompoundTag target = ((IPlayerAttrStorage) client.player).getPersistentData();
            for (String key : data.getAllKeys()) {
                Tag val = data.get(key);
                target.put(key, val);
            }
        }
    }
}

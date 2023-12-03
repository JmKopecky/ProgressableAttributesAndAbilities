package dev.prognitio.pa3;

import dev.prognitio.pa3.packets.FirePrimaryCSPacket;
import dev.prognitio.pa3.packets.FireSecondaryCSPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class FabricModMessages {

    public static final ResourceLocation PRIMARY_PACKET_ID = new ResourceLocation(Constants.MOD_ID, "primarykeyfired");
    public static final ResourceLocation SECONDARY_PACKET_ID = new ResourceLocation(Constants.MOD_ID, "secondarykeyfired");


    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(PRIMARY_PACKET_ID, FirePrimaryCSPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SECONDARY_PACKET_ID, FireSecondaryCSPacket::receive);
    }

    public static void registerS2CPackets() {

    }
}

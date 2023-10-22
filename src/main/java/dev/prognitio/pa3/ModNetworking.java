package dev.prognitio.pa3;

import dev.prognitio.pa3.PrimaryC2SPacket;
import dev.prognitio.pa3.pa3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(pa3.MODID, "networking"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PrimaryC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PrimaryC2SPacket::new)
                .encoder(PrimaryC2SPacket::toBytes)
                .consumerMainThread(PrimaryC2SPacket::handle).add();

        net.messageBuilder(SecondaryCS2Packet.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SecondaryCS2Packet::new)
                .encoder(SecondaryCS2Packet::toBytes)
                .consumerMainThread(SecondaryCS2Packet::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

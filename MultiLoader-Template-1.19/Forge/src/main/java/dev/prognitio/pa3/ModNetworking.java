package dev.prognitio.pa3;

import dev.prognitio.pa3.keybindsystem.PrimaryC2SPacket;
import dev.prognitio.pa3.keybindsystem.SecondaryCS2Packet;
import dev.prognitio.pa3.userhud.SyncCooldownDataSC;
import dev.prognitio.pa3.userhud.SyncPassiveProcSC;
import dev.prognitio.pa3.userinterface.packets.*;
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
                .named(new ResourceLocation(Constants.MOD_ID, "networking"))
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

        net.messageBuilder(SyncAttrLevelSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAttrLevelSC::new)
                .encoder(SyncAttrLevelSC::toBytes)
                .consumerMainThread(SyncAttrLevelSC::handle).add();

        net.messageBuilder(SyncAttrMaxLevelSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAttrMaxLevelSC::new)
                .encoder(SyncAttrMaxLevelSC::toBytes)
                .consumerMainThread(SyncAttrMaxLevelSC::handle).add();

        net.messageBuilder(SyncPointsSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPointsSC::new)
                .encoder(SyncPointsSC::toBytes)
                .consumerMainThread(SyncPointsSC::handle).add();

        net.messageBuilder(SyncTotalLevelSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncTotalLevelSC::new)
                .encoder(SyncTotalLevelSC::toBytes)
                .consumerMainThread(SyncTotalLevelSC::handle).add();

        net.messageBuilder(SyncXPSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncXPSC::new)
                .encoder(SyncXPSC::toBytes)
                .consumerMainThread(SyncXPSC::handle).add();

        net.messageBuilder(SyncXpLevelReqSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncXpLevelReqSC::new)
                .encoder(SyncXpLevelReqSC::toBytes)
                .consumerMainThread(SyncXpLevelReqSC::handle).add();

        net.messageBuilder(SyncAbilLevelSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAbilLevelSC::new)
                .encoder(SyncAbilLevelSC::toBytes)
                .consumerMainThread(SyncAbilLevelSC::handle).add();

        net.messageBuilder(SyncAbilMaxLevelSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAbilMaxLevelSC::new)
                .encoder(SyncAbilMaxLevelSC::toBytes)
                .consumerMainThread(SyncAbilMaxLevelSC::handle).add();

        net.messageBuilder(SyncSelectedAbililtySC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSelectedAbililtySC::new)
                .encoder(SyncSelectedAbililtySC::toBytes)
                .consumerMainThread(SyncSelectedAbililtySC::handle).add();

        net.messageBuilder(SyncAbilUpgCostSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAbilUpgCostSC::new)
                .encoder(SyncAbilUpgCostSC::toBytes)
                .consumerMainThread(SyncAbilUpgCostSC::handle).add();

        net.messageBuilder(SyncAttrUpgCostSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAttrUpgCostSC::new)
                .encoder(SyncAttrUpgCostSC::toBytes)
                .consumerMainThread(SyncAttrUpgCostSC::handle).add();

        net.messageBuilder(SyncAbilEliteSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAbilEliteSC::new)
                .encoder(SyncAbilEliteSC::toBytes)
                .consumerMainThread(SyncAbilEliteSC::handle).add();

        net.messageBuilder(SyncCooldownDataSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncCooldownDataSC::new)
                .encoder(SyncCooldownDataSC::toBytes)
                .consumerMainThread(SyncCooldownDataSC::handle).add();

        net.messageBuilder(SyncPassiveProcSC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPassiveProcSC::new)
                .encoder(SyncPassiveProcSC::toBytes)
                .consumerMainThread(SyncPassiveProcSC::handle).add();

        net.messageBuilder(LevelUpAttributeCS.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LevelUpAttributeCS::new)
                .encoder(LevelUpAttributeCS::toBytes)
                .consumerMainThread(LevelUpAttributeCS::handle).add();

        net.messageBuilder(LevelUpAbilityCS.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LevelUpAbilityCS::new)
                .encoder(LevelUpAbilityCS::toBytes)
                .consumerMainThread(LevelUpAbilityCS::handle).add();

        net.messageBuilder(SetSelectedAbilityCS.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetSelectedAbilityCS::new)
                .encoder(SetSelectedAbilityCS::toBytes)
                .consumerMainThread(SetSelectedAbilityCS::handle).add();

        net.messageBuilder(UnlockEliteAbilityCS.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UnlockEliteAbilityCS::new)
                .encoder(UnlockEliteAbilityCS::toBytes)
                .consumerMainThread(UnlockEliteAbilityCS::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

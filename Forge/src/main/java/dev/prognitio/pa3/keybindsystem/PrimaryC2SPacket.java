package dev.prognitio.pa3.keybindsystem;

import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PrimaryC2SPacket {

    public PrimaryC2SPacket() {

    }

    public PrimaryC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //on the server
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> cap.firePrimaryAbility(player));
            }
        });
        return true;
    }
}

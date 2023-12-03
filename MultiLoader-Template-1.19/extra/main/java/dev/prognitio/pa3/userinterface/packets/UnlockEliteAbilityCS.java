package dev.prognitio.pa3.userinterface.packets;

import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UnlockEliteAbilityCS {

    private final String data;

    public UnlockEliteAbilityCS(String data) {
        this.data = data;
    }
    public UnlockEliteAbilityCS(FriendlyByteBuf buf) {
        this.data = buf.readComponent().getString();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(Component.literal(data));

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            player.getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                cap.attemptUnlockElite(data);
                cap.syncDataToPlayer(player);
            });
        });
        return true;
    }
}

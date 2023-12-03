package dev.prognitio.pa3.userinterface.packets;

import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetSelectedAbilityCS {

    private final String data;

    public SetSelectedAbilityCS(String data) {
        this.data = data;
    }
    public SetSelectedAbilityCS(FriendlyByteBuf buf) {
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
                String ability = data.split(":")[0];
                String mode = data.split(":")[1];
                if (mode.equals("p")) {
                    cap.setPrimaryAbility(ability);
                } else if (mode.equals("s")) {
                    cap.setSecondaryAbility(ability);
                }
                cap.syncDataToPlayer(player);
            });
        });
        return true;
    }
}

package dev.prognitio.pa3.userinterface.packets;

import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LevelUpAbilityCS {

    private final String data;

    public LevelUpAbilityCS(String data) {
        this.data = data;
    }
    public LevelUpAbilityCS(FriendlyByteBuf buf) {
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
                switch (data) {
                    case "dash" -> {
                        if (cap.dash.level == 0) {
                            cap.dash.attemptPurchaseAbility(cap.getAvailablePoints());
                        } else {
                            cap.dash.attemptLevelAbility(cap.getAvailablePoints());
                        }
                    }
                    case "arrowsalvo" -> {
                        if (cap.arrowSalvo.level == 0) {
                            cap.arrowSalvo.attemptPurchaseAbility(cap.getAvailablePoints());
                        } else {
                            cap.arrowSalvo.attemptLevelAbility(cap.getAvailablePoints());
                        }
                    }
                    case "overshield" -> {
                        if (cap.overshield.level == 0) {
                            cap.overshield.attemptPurchaseAbility(cap.getAvailablePoints());
                        } else {
                            cap.overshield.attemptLevelAbility(cap.getAvailablePoints());
                        }
                    }
                }
                cap.syncDataToPlayer(player);
            });
        });
        return true;
    }
}

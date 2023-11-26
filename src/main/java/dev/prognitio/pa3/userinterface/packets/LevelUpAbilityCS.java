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
                        int result;
                        if (cap.dash.level == 0) {
                            result = cap.dash.attemptPurchaseAbility(cap.getAvailablePoints());
                        } else {
                            result = cap.dash.attemptLevelAbility(cap.getAvailablePoints());
                        }
                        cap.setAvailablePoints(cap.getAvailablePoints() - result);
                    }
                    case "arrowsalvo" -> {
                        int result;
                        if (cap.arrowSalvo.level == 0) {
                            result = cap.arrowSalvo.attemptPurchaseAbility(cap.getAvailablePoints());
                        } else {
                            result = cap.arrowSalvo.attemptLevelAbility(cap.getAvailablePoints());
                        }
                        cap.setAvailablePoints(cap.getAvailablePoints() - result);
                    }
                    case "overshield" -> {
                        int result;
                        if (cap.overshield.level == 0) {
                            result = cap.overshield.attemptPurchaseAbility(cap.getAvailablePoints());
                        } else {
                            result = cap.overshield.attemptLevelAbility(cap.getAvailablePoints());
                        }
                        cap.setAvailablePoints(cap.getAvailablePoints() - result);
                    }
                }
                cap.syncDataToPlayer(player);
            });
        });
        return true;
    }
}

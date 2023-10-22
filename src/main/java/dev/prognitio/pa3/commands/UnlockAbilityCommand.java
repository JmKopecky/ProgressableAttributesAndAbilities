package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.prognitio.pa3.AbilityType;
import dev.prognitio.pa3.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class UnlockAbilityCommand {

    public UnlockAbilityCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("unlockability").then(Commands.argument("ability", StringArgumentType.string()).executes(
                (context) -> commandTask(context.getSource(), StringArgumentType.getString(context, "ability"))
        ))));
    }

    private int commandTask(CommandSourceStack context, String ability) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
            if (AbilityType.isValidAbility(ability)) {
                int cost = cap.getAbilityFromString(ability).attemptPurchaseAbility(cap.getAvailablePoints());
                if (cost < 0) {
                    context.getPlayer().sendSystemMessage(Component.literal("Failed to unlock ability"));
                } else {
                    context.getPlayer().sendSystemMessage(Component.literal("Unlocked " + ability));
                }
            } else {
                context.getPlayer().sendSystemMessage(Component.literal("Failed to unlock ability"));
            }
        });
        return 1;
    }

}

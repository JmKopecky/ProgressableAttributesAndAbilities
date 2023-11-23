package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.prognitio.pa3.capabililty.AbilityType;
import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class LevelAbilityCommand {

    public LevelAbilityCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("levelability").then(Commands.argument("ability", StringArgumentType.string()).executes(
                (context) -> commandTask(context.getSource(), StringArgumentType.getString(context, "ability"))
        ))));
    }

    private int commandTask(CommandSourceStack context, String ability) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
            if (AbilityType.isValidAbility(ability)) {
                int cost = cap.getAbilityFromString(ability).attemptLevelAbility(cap.getAvailablePoints());
                if (cost < 0) {
                    context.getPlayer().sendSystemMessage(Component.literal("Failed to level up ability"));
                } else {
                    context.getPlayer().sendSystemMessage(Component.literal("Leveled up " + ability));
                }
            } else {
                context.getPlayer().sendSystemMessage(Component.literal("Failed to level up ability"));
            }
        });
        return 1;
    }

}

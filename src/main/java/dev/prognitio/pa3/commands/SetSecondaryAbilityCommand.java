package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.prognitio.pa3.capabililty.AbilityType;
import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class SetSecondaryAbilityCommand {

    public SetSecondaryAbilityCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("setsecondary").then(Commands.argument("ability", StringArgumentType.string()).executes(
                (context) -> commandTask(context.getSource(), StringArgumentType.getString(context, "ability"))
        ))));
    }

    private int commandTask(CommandSourceStack context, String ability) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
            if (AbilityType.isValidAbility(ability)) {
                cap.setSecondaryAbility(ability);
                context.getPlayer().sendSystemMessage(Component.literal("Set secondary ability to " + ability));
            } else {
                context.getPlayer().sendSystemMessage(Component.literal("Failed to set secondary ability"));
            }
        });
        return 1;
    }

}

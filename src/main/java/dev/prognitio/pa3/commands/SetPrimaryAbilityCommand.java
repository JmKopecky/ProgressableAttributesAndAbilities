package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.prognitio.pa3.AbilityType;
import dev.prognitio.pa3.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class SetPrimaryAbilityCommand {

    public SetPrimaryAbilityCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("setprimary").then(Commands.argument("ability", StringArgumentType.string()).executes(
                (context) -> commandTask(context.getSource(), StringArgumentType.getString(context, "ability"))
        ))));
    }

    private int commandTask(CommandSourceStack context, String ability) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
            if (AbilityType.isValidAbility(ability)) {
                cap.setPrimaryAbility(ability);
                context.getPlayer().sendSystemMessage(Component.literal("Set primary ability to " + ability));
            } else {
                context.getPlayer().sendSystemMessage(Component.literal("Failed to set primary ability"));
            }
        });
        return 1;
    }

}

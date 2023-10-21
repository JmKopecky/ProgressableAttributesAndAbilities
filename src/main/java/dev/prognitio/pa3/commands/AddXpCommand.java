package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.prognitio.pa3.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class AddXpCommand {

    public AddXpCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("addxp").then(Commands.argument("experience", IntegerArgumentType.integer()).executes(
                (context) -> commandTask(context.getSource(), IntegerArgumentType.getInteger(context, "experience"))
        ))));
    }

    private int commandTask(CommandSourceStack context, int experience) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
            cap.addXP(experience);
            context.getPlayer().sendSystemMessage(Component.literal("Added " + experience + " experience"));
        });
        return 1;
    }
}

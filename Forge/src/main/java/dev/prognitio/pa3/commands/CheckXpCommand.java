package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class CheckXpCommand {

    public CheckXpCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("checkxp").executes(
                (context) -> commandTask(context.getSource())
        )));
    }

    private int commandTask(CommandSourceStack context) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) ->
                context.getPlayer().sendSystemMessage(Component.literal(
                        "lvl " + cap.getLevel() + " | " + cap.getExperience() + "exp | " + cap.getAvailablePoints() + " points")));
        return 1;
    }
}

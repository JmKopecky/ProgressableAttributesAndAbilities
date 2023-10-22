package dev.prognitio.pa3.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.prognitio.pa3.AttributesProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class LevelUpAttrCommand {

    public LevelUpAttrCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("progressattr").then(Commands.literal("levelattr").then(Commands.argument("attribute", StringArgumentType.string()).executes(
                (context) -> commandTask(context.getSource(), StringArgumentType.getString(context, "attribute"))
        ))));
    }

    private int commandTask(CommandSourceStack context, String type) {
        Objects.requireNonNull(context.getPlayer()).getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
            boolean result = cap.attemptLevelUpAttribute(type);
            if (result) {
                context.getPlayer().sendSystemMessage(Component.literal("Successfully leveled up the " + type + " attribute"));
            } else {
                context.getPlayer().sendSystemMessage(Component.literal("Failed to level up the " + type + " attribute. Do you have enough attribute points, and does the attribute exist?"));
            }
            cap.applyApplicableAttributes(context.getPlayer());
        });
        return 1;
    }
}

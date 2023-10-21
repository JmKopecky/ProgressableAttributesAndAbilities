package dev.prognitio.pa3;


import dev.prognitio.pa3.commands.AddXpCommand;
import dev.prognitio.pa3.commands.CheckXpCommand;
import dev.prognitio.pa3.commands.LevelUpAttrCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid=pa3.MODID)
public class ModEvents {


    //example for adding xp for breaking blocks
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null) {
            event.getPlayer().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((attributes -> attributes.addXP(5)));
        }
    }
    /*
    other ideas:
    AdvancementEvent
    EntityPlaceEvent
    ItemCraftedEvent
    ItemSmeltedEvent
    LivingDeathEvent
     */



    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (event.getObject() != null) {
                if (!(event.getObject()).getCapability(AttributesProvider.ATTRIBUTES).isPresent()) {
                    event.addCapability(new ResourceLocation(pa3.MODID, "properties_attribute_system"), new AttributesProvider());
                }

            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(original -> event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cloned -> cloned.copyFrom(original)));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AttributesCompatabililty.class);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        new CheckXpCommand(event.getDispatcher());
        new AddXpCommand(event.getDispatcher());
        new LevelUpAttrCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(AttributesProvider.ATTRIBUTES).ifPresent((attributes ->
                attributes.applyApplicableAttributes(event.player)));
    }
}

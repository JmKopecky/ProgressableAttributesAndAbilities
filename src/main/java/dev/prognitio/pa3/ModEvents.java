package dev.prognitio.pa3;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=pa3.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Player> event) {
        if (event.getObject() != null) {
            if (!(event.getObject()).getCapability(AttributesProvider.ATTRIBUTES).isPresent()) {
                event.addCapability(new ResourceLocation(pa3.MODID, "properties_attribute_system"), new AttributesProvider());
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(original -> {
                event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cloned -> {
                    cloned.copyFrom(original);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Attributes.class);
    }
}

package dev.prognitio.pa3;

import dev.prognitio.pa3.keybindsystem.Keybinding;
import dev.prognitio.pa3.userhud.HudOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = pa3.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModBusEvents {
    @SubscribeEvent
    public static void renderGUIOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("cooldowninfohud", HudOverlay.HUD_PA3);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(Keybinding.PRIMARY_ABILITY_KEY);
        event.register(Keybinding.SECONDARY_ABILITY_KEY);
        event.register(Keybinding.OPEN_INTERFACE_KEY);
    }
}

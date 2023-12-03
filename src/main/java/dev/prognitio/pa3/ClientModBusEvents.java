package dev.prognitio.pa3;

import dev.prognitio.pa3.entity.ChainLightningRenderer;
import dev.prognitio.pa3.entity.EntityRegister;
import dev.prognitio.pa3.entity.IncendiaryLanceRenderer;
import dev.prognitio.pa3.keybindsystem.Keybinding;
import dev.prognitio.pa3.userhud.HudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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

    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegister.CHAIN_LIGHTNING.get(), ChainLightningRenderer::new);
        EntityRenderers.register(EntityRegister.INCENDIARY_LANCE.get(), IncendiaryLanceRenderer::new);
    }


    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.register(ParticleRegister.DEFENSIVE_SHIELD_PARTICLE.get(), DeflectiveShieldParticle.Provider::new);
    }
}

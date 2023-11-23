package dev.prognitio.pa3.keybindsystem;

import dev.prognitio.pa3.ModNetworking;
import dev.prognitio.pa3.pa3;
import dev.prognitio.pa3.userinterface.AttributeDisplayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = pa3.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (Keybinding.PRIMARY_ABILITY_KEY.consumeClick()) {
                ModNetworking.sendToServer(new PrimaryC2SPacket());
            }
            if (Keybinding.SECONDARY_ABILITY_KEY.consumeClick()) {
                ModNetworking.sendToServer(new SecondaryCS2Packet());
            }
            if (Keybinding.OPEN_INTERFACE_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(new AttributeDisplayScreen(Component.literal("Attribute Interface")));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = pa3.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Keybinding.PRIMARY_ABILITY_KEY);
            event.register(Keybinding.SECONDARY_ABILITY_KEY);
            event.register(Keybinding.OPEN_INTERFACE_KEY);
        }
    }
}

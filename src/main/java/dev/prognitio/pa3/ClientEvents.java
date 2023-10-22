package dev.prognitio.pa3;

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
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Primary Key Pressed"));
                ModNetworking.sendToServer(new PrimaryC2SPacket());
            }
            if (Keybinding.SECONDARY_ABILITY_KEY.consumeClick()) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Secondary Key Pressed"));
                ModNetworking.sendToServer(new SecondaryCS2Packet());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = pa3.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Keybinding.PRIMARY_ABILITY_KEY);
            event.register(Keybinding.SECONDARY_ABILITY_KEY);
        }
    }
}

package dev.prognitio.pa3;

import dev.prognitio.pa3.keybindsystem.Keybinding;
import dev.prognitio.pa3.keybindsystem.PrimaryC2SPacket;
import dev.prognitio.pa3.keybindsystem.SecondaryCS2Packet;
import dev.prognitio.pa3.userinterface.AttributeDisplayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
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
}

package dev.prognitio.pa3.util;

import com.mojang.blaze3d.platform.InputConstants;
import dev.prognitio.pa3.FabricModMessages;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.lwjgl.glfw.GLFW;

import static dev.prognitio.pa3.KeyBindResources.*;

public class KeyInputHandler {

    public static KeyMapping OPEN_INTERFACE_KEY;
    public static KeyMapping FIRE_PRIMARY_KEY;
    public static KeyMapping FIRE_SECONDARY_KEY;


    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(OPEN_INTERFACE_KEY.consumeClick()) {
                System.out.println("Registered Open Interface Key");
            }
            if (FIRE_PRIMARY_KEY.consumeClick()) {
                ClientPlayNetworking.send(FabricModMessages.PRIMARY_PACKET_ID, PacketByteBufs.create());
            }
            if (FIRE_SECONDARY_KEY.consumeClick()) {
                ClientPlayNetworking.send(FabricModMessages.PRIMARY_PACKET_ID, PacketByteBufs.create());
            }
        });
    }

    public static void register() {
        OPEN_INTERFACE_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_OPEN_INTERFACE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY
        ));

        FIRE_PRIMARY_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_ACTIVATE_PRIMARY, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY
        ));

        FIRE_SECONDARY_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_ACTIVATE_SECONDARY, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY
        ));

        registerKeyInputs();
    }
}

package dev.prognitio.pa3;

import dev.prognitio.pa3.client.FabricHudOverlay;
import dev.prognitio.pa3.util.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class Pa3Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        KeyInputHandler.register();
        FabricModMessages.registerS2CPackets();

        HudRenderCallback.EVENT.register(new FabricHudOverlay());
    }
}

package dev.prognitio.pa3;

import dev.prognitio.pa3.util.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class Pa3Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        KeyInputHandler.register();
        FabricModMessages.registerS2CPackets();
    }
}

package dev.prognitio.pa3.userinterface.packets;

import dev.prognitio.pa3.userinterface.ClientDataStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncAttrLevelSC {

    private final String data;

    public SyncAttrLevelSC(String data) {
        this.data = data;
    }
    public SyncAttrLevelSC(FriendlyByteBuf buf) {
        this.data = buf.readComponent().getString();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(Component.literal(data));

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDataStorage.setAttrProperty(data, "level");
        });
        return true;
    }
}

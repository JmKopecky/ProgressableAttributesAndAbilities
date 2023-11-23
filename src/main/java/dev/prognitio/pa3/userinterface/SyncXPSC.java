package dev.prognitio.pa3.userinterface;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncXPSC {

    private final String data;

    public SyncXPSC(String data) {
        this.data = data;
    }
    public SyncXPSC(FriendlyByteBuf buf) {
        this.data = buf.readComponent().getString();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(Component.literal(data));

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDataStorage.setXp(Integer.parseInt(data));
        });
        return true;
    }
}

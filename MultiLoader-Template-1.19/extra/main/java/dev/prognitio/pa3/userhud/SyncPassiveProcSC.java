package dev.prognitio.pa3.userhud;

import dev.prognitio.pa3.ClientDataStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPassiveProcSC {

    private final String data;

    public SyncPassiveProcSC(String data) {
        this.data = data;
    }
    public SyncPassiveProcSC(FriendlyByteBuf buf) {
        this.data = buf.readComponent().getString();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(Component.literal(data));

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ClientDataStorage.setPassiveProcTimer(data));
        return true;
    }
}

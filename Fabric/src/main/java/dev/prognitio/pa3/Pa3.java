package dev.prognitio.pa3;

import dev.prognitio.pa3.event.BlockBrokenHandler;
import dev.prognitio.pa3.event.EntityKilledHandler;
import dev.prognitio.pa3.event.PlayerTickHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class Pa3 implements ModInitializer {
    
    @Override
    public void onInitialize() {

        FabricModMessages.registerC2SPackets();

        ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new EntityKilledHandler());
        PlayerBlockBreakEvents.AFTER.register(new BlockBrokenHandler());
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        
        // Some code like events require special initialization from the
        // loader specific code.
        //ItemTooltipCallback.EVENT.register(CommonClass::onItemTooltip);
    }
}

package dev.prognitio.pa3.event;

import dev.prognitio.pa3.playerdata.PlayerDataGetters;
import dev.prognitio.pa3.playerdata.PlayerDataInteraction;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        //tick event
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            //decrease current cooldown if it is above 0.
            PlayerDataInteraction.setAbilityCooldown(player, PlayerDataGetters.getAbilityCooldown(((IPlayerAttrStorage) player)) - 1);
            //decrease passive timers
            PlayerDataInteraction.decreasePassiveTimers(player);
        }
    }
}

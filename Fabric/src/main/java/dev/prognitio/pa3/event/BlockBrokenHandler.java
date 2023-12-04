package dev.prognitio.pa3.event;

import dev.prognitio.pa3.playerdata.PlayerDataGetters;
import dev.prognitio.pa3.playerdata.PlayerDataInteraction;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBrokenHandler implements PlayerBlockBreakEvents.After{
    @Override
    public void afterBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        PlayerDataInteraction.setExperience(player, PlayerDataGetters.getXp(((IPlayerAttrStorage) player)) + 20);
    }
}

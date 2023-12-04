package dev.prognitio.pa3.event;

import dev.prognitio.pa3.playerdata.PlayerDataGetters;
import dev.prognitio.pa3.playerdata.PlayerDataInteraction;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class EntityKilledHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity {
    @Override
    public void afterKilledOtherEntity(ServerLevel world, Entity entity, LivingEntity killedEntity) {
        if (entity instanceof ServerPlayer) {
            int healthBonus = (int) killedEntity.getMaxHealth() * 10;
            int damageBonus = (int) killedEntity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 2;
            int xp = 10 + healthBonus + damageBonus;
            PlayerDataInteraction.setExperience((Player) entity, PlayerDataGetters.getXp(((IPlayerAttrStorage) entity)) + xp);
        }
    }
}

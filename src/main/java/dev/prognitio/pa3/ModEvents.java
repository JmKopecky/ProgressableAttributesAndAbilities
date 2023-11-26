package dev.prognitio.pa3;


import dev.prognitio.pa3.capabililty.AttributesCapability;
import dev.prognitio.pa3.capabililty.AttributesProvider;
import dev.prognitio.pa3.commands.*;
import dev.prognitio.pa3.effects.EffectsRegister;
import dev.prognitio.pa3.userhud.SyncCooldownDataSC;
import dev.prognitio.pa3.userhud.SyncPassiveProcSC;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Random;

@Mod.EventBusSubscriber(modid=pa3.MODID)
public class ModEvents {

    //example for adding xp for breaking blocks
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null) {
            event.getPlayer().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((attributes -> {
                attributes.addXP(10);
                attributes.syncDataToPlayer(event.getPlayer());
                    }));
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof Player) {
            event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((attributes -> {
                attributes.addXP(10);
                attributes.syncDataToPlayer((Player) event.getEntity());
            }));
        }
    }

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
            event.getSource().getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((attributes -> {
                attributes.addXP(10);
                attributes.syncDataToPlayer((Player) event.getSource().getEntity());
            }));
        }
    }

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() != null) {
            event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((attributes -> {
                attributes.addXP(10);
                attributes.syncDataToPlayer(event.getEntity());
            }));
        }
    }


    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (event.getObject() != null) {
                if (!(event.getObject()).getCapability(AttributesProvider.ATTRIBUTES).isPresent()) {
                    event.addCapability(new ResourceLocation(pa3.MODID, "properties_attribute_system"), new AttributesProvider());
                    event.getObject().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                        cap.applyApplicableAttributes((Player) event.getObject());
                        cap.syncDataToPlayer((Player) event.getObject());
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(original -> event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cloned -> {
                cloned.copyFrom(original);
                cloned.applyApplicableAttributes(event.getEntity());
                cloned.syncDataToPlayer(event.getEntity());
                event.getEntity().setHealth(event.getEntity().getMaxHealth());
            }));
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AttributesCapability.class);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        new CheckXpCommand(event.getDispatcher());
        new AddXpCommand(event.getDispatcher());
        new LevelUpAttrCommand(event.getDispatcher());
        new LevelAbilityCommand(event.getDispatcher());
        new UnlockAbilityCommand(event.getDispatcher());
        new SetPrimaryAbilityCommand(event.getDispatcher());
        new SetSecondaryAbilityCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                    cap.applyApplicableAttributes(event.getEntity());
                    cap.syncDataToPlayer(event.getEntity());
                });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                if (cap.getAbilityCooldown() > 0) {
                    cap.setAbilityCooldown(cap.getAbilityCooldown() - 1);
                    ModNetworking.sendToPlayer(new SyncCooldownDataSC(cap.getAbilityCooldown() + ":" + cap.getCurrentMaxCooldown()), (ServerPlayer) event.player);
                }
                cap.decreasePassiveTimers();
                ModNetworking.sendToPlayer(new SyncPassiveProcSC("dodge:" + cap.getPassiveDodgeProc()), (ServerPlayer) event.player);
                ModNetworking.sendToPlayer(new SyncPassiveProcSC("parry:" + cap.getPassiveParryProc()), (ServerPlayer) event.player);
                ModNetworking.sendToPlayer(new SyncPassiveProcSC("doublestrike:" + cap.getPassiveDoubleStrikeProc()), (ServerPlayer) event.player);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player) {

            if (event.getSource().isFall()) {
                if (event.getEntity().hasEffect(EffectsRegister.FALL_NEGATE_EFFECT.get())) {
                    event.getEntity().removeEffect(EffectsRegister.FALL_NEGATE_EFFECT.get());
                    event.setCanceled(true);
                    return;
                }
            }

            if (event.getEntity().hasEffect(EffectsRegister.ATTACK_NEGATE_EFFECT.get())) {
                event.getEntity().removeEffect(EffectsRegister.ATTACK_NEGATE_EFFECT.get());
                event.setCanceled(true);
                return;
            }

            event.getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                Random random = new Random();
                //dodge
                double dodgechance = cap.nimbleness.calculatePower(1);
                if (random.nextDouble(0, 100) < dodgechance) {
                    //do dodge
                    cap.triggerDodgeProc();
                    event.setCanceled(true);
                    return;
                }
                //parry
                double parryChance = (cap.combat.level / (double) cap.combat.maxLevel) * 75.0;
                double parryAmount = cap.combat.calculatePower(1) / 100;
                if (random.nextDouble(0, 100) < parryChance) {
                    //do parry
                    event.setAmount((float) (event.getAmount() * parryAmount));
                    cap.triggerParryProc();
                }
            });
        }
        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
            event.getSource().getEntity().getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                Random random = new Random();
                if (random.nextDouble(0, 100) < cap.strategy.calculatePower(1)) {
                    //do double strike
                    event.setAmount(event.getAmount() * 2);
                    cap.triggerDoubleStrikeProc();
                }
            });
        }
    }
}

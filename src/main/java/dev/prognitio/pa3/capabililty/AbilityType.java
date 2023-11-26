package dev.prognitio.pa3.capabililty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.prognitio.pa3.effects.EffectsRegister;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class AbilityType {

    String id;
    public int level;
    int maxLevel;
    int cooldown;
    int cooldownScale;
    int purchaseCost;
    int upgradeScale;
    boolean isEliteVersion;
    public static final int ELITE_ABILITY_COST = 10;

    public AbilityType(String id, int maxLevel, int initialCooldown, int cooldownScale, int purchaseCost, int upgradeScale) {
        this.id = id;
        this.maxLevel = maxLevel;
        this.level = 0;
        this.cooldown = initialCooldown;
        this.cooldownScale = cooldownScale;
        this.purchaseCost = purchaseCost;
        this.upgradeScale = upgradeScale;
        this.isEliteVersion = false;
    }

    public int attemptPurchaseAbility(int availablePoints) {
        if (availablePoints >= purchaseCost && level == 0) {
            level = 1;
            return purchaseCost;
        } else {
            return -1;
        }
    }

    public int attemptLevelAbility(int availablePoints) {
        int cost = 1 + level * upgradeScale;
        if (availablePoints >= cost && level != 0 && level != maxLevel) {
            level++;
            this.cooldown += this.cooldownScale;
            return cost;
        } else {
            return -1;
        }
    }

    public int attemptUnlockElite(int availablePoints) {
        if  (level == maxLevel) {
            if (availablePoints >= ELITE_ABILITY_COST) {
                isEliteVersion = true;
                return ELITE_ABILITY_COST;
            }
        }
        return -1;
    }

    public void runAbility(Player player) {
        if (level > 0) {
            switch (id) {
                case "dash" -> dash(player);
                case "arrowsalvo" -> arrowSalvo(player);
                case "overshield" -> overshield(player);
            }

            //add cooldown
            player.getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
                cap.setAbilityCooldown(this.cooldown * 40);
                cap.setCurrentMaxCooldown(this.cooldown * 40);
            });
        }
    }

    public static boolean isValidAbility(String abilityString) {
        switch (abilityString) {
            case "dash", "arrowsalvo", "overshield" -> {return true;}
            default -> {return false;}
        }
    }

    public void dash(Player player) {
        double strength = (level * 0.5) + 0.5;
        Vec3 dir = player.getViewVector(0);
        dir = dir.scale(strength);
        player.setDeltaMovement(dir);
        player.hurtMarked = true;

        if (isEliteVersion) {
            player.addEffect(new MobEffectInstance(EffectsRegister.FALL_NEGATE_EFFECT.get(), 30 * 20, 0));
        }
    }

    public void arrowSalvo(Player player) {
        Random random = new Random();
        int arrowCount = random.nextInt(12, 12 + (level * 7));
        for (int i = 0; i < arrowCount; i++) {
            System.out.println("Spawning an arrow");
            AbstractArrow arrow = new AbstractArrow(EntityType.ARROW, player, player.level) {
                @Override
                protected ItemStack getPickupItem() {
                    return ItemStack.EMPTY;
                }
            };
            float xRot = player.getXRot();
            xRot += random.nextFloat(0, 20);
            xRot -= random.nextFloat(0, 20);
            float yRot = player.getYRot();
            yRot += random.nextFloat(0, 20);
            yRot -= random.nextFloat(0, 20);
            float vel = random.nextFloat(1, 4);
            arrow.shootFromRotation(player, xRot, yRot, 0.0f, vel, 0.5f);
            arrow.setBaseDamage(arrow.getBaseDamage() + 2);
            arrow.setCritArrow(true);
            player.level.addFreshEntity(arrow);
        }
        if (isEliteVersion) {
            player.addEffect(new MobEffectInstance(EffectsRegister.ARROW_SCATTER_EFFECT.get(), 20, 1));
        }
    }

    public void overshield(Player player) {
        int duration = (level * 10 + 10) * 20;
        int modifier = level * 2 - 1;
        if (player.getAbsorptionAmount() == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, modifier));
        }

        if (isEliteVersion) {
            player.addEffect(new MobEffectInstance(EffectsRegister.ATTACK_NEGATE_EFFECT.get(), 30 * 20, 0));
        }
    }

    public static AbilityType fromString(String GSON) {
        return ((new GsonBuilder()).create()).fromJson(GSON, AbilityType.class);
    }

    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}

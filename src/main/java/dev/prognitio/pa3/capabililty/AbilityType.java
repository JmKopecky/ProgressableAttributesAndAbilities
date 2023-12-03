package dev.prognitio.pa3.capabililty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.prognitio.pa3.effects.EffectsRegister;
import dev.prognitio.pa3.entity.ChainLightningProjectile;
import dev.prognitio.pa3.entity.EntityRegister;
import dev.prognitio.pa3.entity.IncendiaryLanceProjectile;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class AbilityType {

    // Note when adding ability: ensure all the ability is added everywhere you see other abilities in the following classes:
    //  - AbilityType.java
    //  - AttributesCapability.java
    //  - ClientDataStorage.java
    //  - AbilityDisplayScreen.java
    //  - LevelUpAbilityCS.java
    // These are the only classes that need updated.

    String id;
    public int level;
    int maxLevel;
    int cooldown;
    int cooldownScale;
    int purchaseCost;
    int upgradeScale;
    public boolean isEliteVersion;
    public static final int ELITE_ABILITY_COST = 3;

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
        int cost = 1 + level/2 * upgradeScale;
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
                case "incendiarylance" -> incendiaryLance(player);
                case "chainlightning" -> chainLightning(player);
                case "deflectiveshield" -> deflectiveShield(player);
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
            case "dash", "arrowsalvo", "overshield", "incendiarylance", "chainlightning", "deflectiveshield" -> {return true;}
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
            AbstractArrow arrow = new AbstractArrow(EntityType.ARROW, player, player.level) {
                @Override
                protected @NotNull ItemStack getPickupItem() {
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
            arrow.setBaseDamage(arrow.getBaseDamage() + 1);
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

    public void incendiaryLance(Player player) {
        IncendiaryLanceProjectile projectile = new IncendiaryLanceProjectile(EntityRegister.INCENDIARY_LANCE.get(), player, player.level);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.0f, 0.9f);
        projectile.setBaseDamage(12 + 4*level);
        projectile.setOwner(player);
        player.level.addFreshEntity(projectile);
    }

    public void chainLightning(Player player) {
        //send a lightning projectile to the closest hostile some number of times

        player.addEffect(new MobEffectInstance(EffectsRegister.CHAIN_LIGHTNING_INVULNERABLE.get(), 100, 0));

        int chainCount = level * 2;
        int range = 4 + level * 2;

        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();
        AABB area = new AABB(
                playerX + range, playerY + range, playerZ + range, playerX - range, playerY - range, playerZ - range);
        ArrayList<LivingEntity> entities = (ArrayList<LivingEntity>) player.level.getEntitiesOfClass(LivingEntity.class, area);
        ArrayList<LivingEntity> targetList = new ArrayList<>();

        for (LivingEntity filteredTarget : entities) {
            if (!filteredTarget.hasEffect(EffectsRegister.CHAIN_LIGHTNING_INVULNERABLE.get())) {
                targetList.add(filteredTarget);
            }
        }

        if (targetList.isEmpty()) {
            ChainLightningProjectile projectile = new ChainLightningProjectile(EntityRegister.CHAIN_LIGHTNING.get(), player, player.level, 1, player);
            projectile.setOwner(player);
            projectile.shootFromRotation(player, player.getRotationVector().x, player.getRotationVector().y, 0, 0.75f, 1f);
            player.level.addFreshEntity(projectile);
            return;
        }

        LivingEntity target = player.level.getNearestEntity(targetList, TargetingConditions.forCombat(), null, playerX, playerY, playerZ);

        AbstractArrow getPos = new AbstractArrow(EntityType.ARROW, player, player.level) {
            @Override
            protected ItemStack getPickupItem() {
                return null;
            }
        };

        getPos.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        Vec2 lookRot = new Vec2(getPos.getXRot(), getPos.getYRot());
        getPos.discard();

        ChainLightningProjectile projectile = new ChainLightningProjectile(EntityRegister.CHAIN_LIGHTNING.get(), player, player.level, 1, target);
        projectile.setOwner(player);
        projectile.shootFromRotation(player, lookRot.x, lookRot.y, 0, 0.75f, 1f);
        player.level.addFreshEntity(projectile);
    }

    public void deflectiveShield(Player player) {
        int duration = 1 + level * 2;
        duration *= 20;
        int amplification = level - 1;
        player.addEffect(new MobEffectInstance(EffectsRegister.DEFENSIVE_SHIELD_EFFECT.get(), duration, amplification));
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

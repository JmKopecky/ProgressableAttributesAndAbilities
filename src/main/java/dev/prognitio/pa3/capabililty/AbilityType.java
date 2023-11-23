package dev.prognitio.pa3.capabililty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class AbilityType {

    String id;
    int level;
    int maxLevel;
    int cooldown;
    int cooldownScale;
    int purchaseCost;
    int upgradeScale;

    public AbilityType(String id, int maxLevel, int initialCooldown, int cooldownScale, int purchaseCost, int upgradeScale) {
        this.id = id;
        this.maxLevel = maxLevel;
        this.level = 0;
        this.cooldown = initialCooldown;
        this.cooldownScale = cooldownScale;
        this.purchaseCost = purchaseCost;
        this.upgradeScale = upgradeScale;
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
            cooldown += cooldownScale;
            return cost;
        } else {
            return -1;
        }
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
                cap.setAbilityCooldown(cooldown * 20);
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
    }

    public void arrowSalvo(Player player) {
        Random random = new Random();
        int arrowCount = random.nextInt(5, 5 + (level * 3));
        for (int i = 0; i < arrowCount; i++) {
            System.out.println("Spawning an arrow");
            AbstractArrow arrow = new AbstractArrow(EntityType.ARROW, player, player.level) {
                @Override
                protected ItemStack getPickupItem() {
                    return Items.ARROW.getDefaultInstance();
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
    }

    public void overshield(Player player) {
        //add an absorption effect
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

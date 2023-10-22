package dev.prognitio.pa3;

import dev.prognitio.pa3.AttributesProvider;
import net.minecraft.world.entity.player.Player;

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
        switch (id) {
            case "dash" -> dash(player);
            case "arrowsalvo" -> arrowSalvo(player);
        }
        //add cooldown
        player.getCapability(AttributesProvider.ATTRIBUTES).ifPresent(cap -> {
            cap.setAbilityCooldown(cooldown * 20);
        });
    }

    public static boolean isValidAbility(String abilityString) {
        switch (abilityString) {
            case "dash", "arrowsalvo" -> {return true;}
            default -> {return false;}
        }
    }

    public void dash(Player player) {
        System.out.println("dashed");
    }

    public void arrowSalvo(Player player) {
        System.out.println("loosed a salvo of arrows");
    }


}

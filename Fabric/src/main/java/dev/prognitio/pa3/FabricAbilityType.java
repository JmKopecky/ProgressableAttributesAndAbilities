package dev.prognitio.pa3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.minecraft.world.entity.player.Player;

public class FabricAbilityType {

    String id;
    public int level;
    int maxLevel;
    int cooldown;
    int cooldownScale;
    int purchaseCost;
    int upgradeScale;
    public boolean isEliteVersion;
    public static final int ELITE_ABILITY_COST = 3;

    public FabricAbilityType(String id, int maxLevel, int initialCooldown, int cooldownScale, int purchaseCost, int upgradeScale) {
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

    public static boolean isValidAbility(String abilityString) {
        switch (abilityString) {
            case "dash", "arrowsalvo", "overshield", "incendiarylance", "chainlightning", "deflectiveshield" -> {return true;}
            default -> {return false;}
        }
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

        }
    }

    public void dash(Player player) {
        System.out.println("dash triggered");
    }

    public void arrowSalvo(Player player) {
        System.out.println("arrow salvo triggered");
    }

    public void overshield(Player player) {
        System.out.println("overshield triggered");
    }

    public void incendiaryLance(Player player) {
        System.out.println("incendiary lance triggered");
    }

    public void chainLightning(Player player) {
        System.out.println("chain lightning triggered");
    }

    public void deflectiveShield(Player player) {
        System.out.println("deflective shield triggered");
    }

    public static FabricAbilityType fromString(String GSON) {
        return ((new GsonBuilder()).create()).fromJson(GSON, FabricAbilityType.class);
    }

    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}

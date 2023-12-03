package dev.prognitio.pa3.playerdata;

import dev.prognitio.pa3.AttributeType;
import dev.prognitio.pa3.mixin.PlayerDataStorageMixin;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PlayerDataInteraction {

    public static boolean attemptLevelUpAttribute(IPlayerAttrStorage player, String attr) {
        AttributeType target;
        switch (attr) {
            case "fitness" -> {
                target = PlayerDataGetters.getFitness(player);
            }
            case "resilience" -> {
                target = PlayerDataGetters.getResilience(player);
            }
            case "nimbleness" -> {
                target = PlayerDataGetters.getNimbleness(player);
            }
            case "combat" -> {
                target = PlayerDataGetters.getCombat(player);
            }
            case "strategy" -> {
                target = PlayerDataGetters.getStrategy(player);
            }
            default -> {
                return false;
            }
        }
        int result = target.attemptLevelUp(PlayerDataGetters.getAvailablePoints(player));
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public static void setAvailablePoints(IPlayerAttrStorage player, int amount) {
        CompoundTag tag = player.getPersistentData();
        tag.putInt("availablepoints", amount);
    }

    public static void setExperience(IPlayerAttrStorage player, int amount) {
        player.getPersistentData().putInt("experience", amount);
    }

    public static void setLevel(IPlayerAttrStorage player, int level) {
        player.getPersistentData().putInt("level", level);
    }

    public static void setAbilityCooldown(IPlayerAttrStorage player, int amount) {
        player.getPersistentData().putInt("cooldown", amount);
    }

    public static void setMaxCooldown(IPlayerAttrStorage player, int amount) {
        player.getPersistentData().putInt("maxcooldown", amount);
    }

    public static void triggerPassiveProc(IPlayerAttrStorage player, String type) {
        player.getPersistentData().putInt("passive" + type, 40);
    }

    public static void decreasePassiveTimers(IPlayerAttrStorage player) {
        player.getPersistentData().putInt("passivedodge", PlayerDataGetters.getPassiveDodge(player));
        player.getPersistentData().putInt("passiveparry", PlayerDataGetters.getPassiveParry(player));
        player.getPersistentData().putInt("passivedoublestrike", PlayerDataGetters.getPassiveDoubleStrike(player));
    }

    public static void attemptUnlockElite(IPlayerAttrStorage player, String target) {
        int availablePoints = PlayerDataGetters.getAvailablePoints(player);
        int result = PlayerDataGetters.getAbilityFromString(player, target).attemptUnlockElite(availablePoints);
        if (availablePoints - result <= availablePoints) {
            setAvailablePoints(player, availablePoints - result);
        }
    }

    public static int calculateLevelUpRequirement(IPlayerAttrStorage player) {
        int level = PlayerDataGetters.getLevel(player);
        //for each level, make it 2% harder to get another level
        int initialXPRequirement = 1000; //what should the base xp requirement be to go from level 0 to level 1
        float perLevelScaling = 1.02f; //how much the exp requirement should increase each level.
        return (int) (initialXPRequirement * (Math.pow(perLevelScaling, level)));
    }

    public static void setPrimaryAbil(IPlayerAttrStorage player, String ability) {
        player.getPersistentData().putString("primaryabil", ability);
    }

    public static void setSecondaryAbil(IPlayerAttrStorage player, String ability) {
        player.getPersistentData().putString("secondaryabil", ability);
    }

    public static void fireAbil(Player player, String type) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        if (PlayerDataGetters.getAbilityCooldown(data) <= 0) {
            String abil = (type.equals("primary")) ? PlayerDataGetters.getPrimaryAbility(data) : PlayerDataGetters.getSecondaryAbility(data);
            PlayerDataGetters.getAbilityFromString(data, abil).runAbility(player);
            data.getPersistentData().putString("lastusedabil", abil);
        }
    }

    public void addXP(int experience, IPlayerAttrStorage player) {
        //if experience is not enough to level up, add to experience. else, level up, zero out experience, add the amount extra after leveling up to experience
        int levelUpDistance = calculateLevelUpRequirement(player) - PlayerDataGetters.getXp(player);
        if (levelUpDistance > experience) {
            //add xp, don't level up
            setExperience(player, PlayerDataGetters.getXp(player) + experience);
        } else {
            setLevel(player, PlayerDataGetters.getLevel(player) + 1);
            setAvailablePoints(player, PlayerDataGetters.getAvailablePoints(player) + 1);
            setExperience(player, experience - levelUpDistance);
            while (PlayerDataGetters.getXp(player) > calculateLevelUpRequirement(player)) {
                setExperience(player, PlayerDataGetters.getXp(player) - calculateLevelUpRequirement(player));
                setLevel(player, PlayerDataGetters.getLevel(player) + 1);
                setAvailablePoints(player, PlayerDataGetters.getAvailablePoints(player) + 1);
            }
        }
    }
}

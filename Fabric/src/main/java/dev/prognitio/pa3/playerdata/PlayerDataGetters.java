package dev.prognitio.pa3.playerdata;

import dev.prognitio.pa3.AttributeType;
import dev.prognitio.pa3.FabricAbilityType;
import dev.prognitio.pa3.util.IPlayerAttrStorage;

public class PlayerDataGetters {

    public static int getLevel(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("level");
    }

    public static int getXp(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("experience");
    }

    public static int getAvailablePoints(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("availablepoints");
    }

    public static int getAbilityCooldown(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("cooldown");
    }

    public static int getCurrentMaxCooldown(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("maxcooldown");
    }

    public static String getPrimaryAbility(IPlayerAttrStorage player) {
        return player.getPersistentData().getString("primaryabil");
    }

    public static String getSecondaryAbility(IPlayerAttrStorage player) {
        return player.getPersistentData().getString("secondaryabil");
    }

    public static String getLastUsedAbility(IPlayerAttrStorage player) {
        return player.getPersistentData().getString("lastusedabil");
    }

    public static AttributeType getFitness(IPlayerAttrStorage player) {
        return AttributeType.fromString(player.getPersistentData().getString("fitness"));
    }

    public static AttributeType getResilience(IPlayerAttrStorage player) {
        return AttributeType.fromString(player.getPersistentData().getString("resilience"));
    }

    public static AttributeType getCombat(IPlayerAttrStorage player) {
        return AttributeType.fromString(player.getPersistentData().getString("combat"));
    }

    public static AttributeType getNimbleness(IPlayerAttrStorage player) {
        return AttributeType.fromString(player.getPersistentData().getString("nimbleness"));
    }

    public static AttributeType getStrategy(IPlayerAttrStorage player) {
        return AttributeType.fromString(player.getPersistentData().getString("strategy"));
    }

    public static int getPassiveDodge(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("passivedodge");
    }

    public static int getPassiveParry(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("passiveparry");
    }

    public static int getPassiveDoubleStrike(IPlayerAttrStorage player) {
        return player.getPersistentData().getInt("passivedoublestrike");
    }

    public static FabricAbilityType getAbilityFromString(IPlayerAttrStorage player, String target) {
        return FabricAbilityType.fromString(player.getPersistentData().getString(target));
    }
}

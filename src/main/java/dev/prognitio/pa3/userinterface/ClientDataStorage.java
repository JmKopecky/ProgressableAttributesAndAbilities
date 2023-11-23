package dev.prognitio.pa3.userinterface;

import java.util.HashMap;
import java.util.Map;

public class ClientDataStorage {

    private static int level;
    private static int xp;
    private static int requiredXp;
    private static int availablePoints;
    private static int fitnessLevel;
    private static int fitnessMaxLevel;
    private static int resilienceLevel;
    private static int resilienceMaxLevel;
    private static int combatLevel;
    private static int combatMaxLevel;
    private static int nimblenessLevel;
    private static int nimblenessMaxLevel;
    private static int strategyLevel;
    private static int strategyMaxLevel;
    private static String primaryAbility;
    private static String secondaryAbility;
    private static Map<String, Integer> abilityLevels = new HashMap<>();
    private static Map<String, Integer> abilityMaxLevels = new HashMap<>();
    private static Map<String, Integer> abilityLevelupReqs = new HashMap<>();
    private static Map<String, Integer> attrLevelupReqs = new HashMap<>();

    public static void setAttrProperty(String packetInput, String property) {
        //packetInput should be in format "type:level"
        String type = packetInput.split(":")[0];
        int level = Integer.parseInt(packetInput.split(":")[1]);
        if (property.equals("level")) {
            switch (type) {
                case "fitness" -> {
                    fitnessLevel = level;
                }
                case "resilience" -> {
                    resilienceLevel = level;
                }
                case "combat" -> {
                    combatLevel = level;
                }
                case "nimbleness" -> {
                    nimblenessLevel = level;
                }
                case "strategy" -> {
                    strategyLevel = level;
                }
            }
        }
        if (property.equals("max")) {
            switch (type) {
                case "fitness" -> {
                    fitnessMaxLevel = level;
                }
                case "resilience" -> {
                    resilienceMaxLevel = level;
                }
                case "combat" -> {
                    combatMaxLevel = level;
                }
                case "nimbleness" -> {
                    nimblenessMaxLevel = level;
                }
                case "strategy" -> {
                    strategyMaxLevel = level;
                }
            }
        }
        if (property.equals("upgrade")) {
            attrLevelupReqs.put(type, level);
        }
    }

    public static int getAttrProperty(String attr, String target) {
        attr = attr.toLowerCase();
        if (target.equals("level")) {
            switch (attr) {
                case "fitness" -> {
                    return fitnessLevel;
                }
                case "resilience" -> {
                    return resilienceLevel;
                }
                case "combat" -> {
                    return combatLevel;
                }
                case "strategy" -> {
                    return strategyLevel;
                }
                case "nimbleness" -> {
                    return nimblenessLevel;
                }
            }
        }
        if (target.equals("max")) {
            switch (attr) {
                case "fitness" -> {
                    return fitnessMaxLevel;
                }
                case "resilience" -> {
                    return resilienceMaxLevel;
                }
                case "combat" -> {
                    return combatMaxLevel;
                }
                case "strategy" -> {
                    return strategyMaxLevel;
                }
                case "nimbleness" -> {
                    return nimblenessMaxLevel;
                }
            }
        }
        if (target.equals("upgrade")) {
            return attrLevelupReqs.get(attr);
        }
        return -1;
    }

    public static String getAbilityProperty(String target, String type) {
        if (type.equals("level")) {
            return String.valueOf(abilityLevels.get(target));
        }
        if (type.equals("max")) {
            return String.valueOf(abilityMaxLevels.get(target));
        }
        if (type.equals("upgrade")) {
            return String.valueOf(abilityLevelupReqs.get(target));
        }
        if (type.equals("desc")) {
            switch (target) {
                case "dash" -> {
                    return "Dash in the direction you are looking";
                }
                case "arrowsalvo" -> {
                    return "Loose a salvo of arrows";
                }
                case "overshield" -> {
                    return "Gain a number of Absorption hearts";
                }
            }
        }
        return "ERROR";
    }

    public static void setAbilityProperty(String target, String type) {
        int value = Integer.parseInt(target.split(":")[1]);
        target = target.split(":")[0];
        if (type.equals("level")) {
            abilityLevels.put(target, value);
        }
        if (type.equals("max")) {
            abilityMaxLevels.put(target, value);
        }
        if (type.equals("upgrade")) {
            abilityLevelupReqs.put(target, value);
        }
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        ClientDataStorage.level = level;
    }

    public static int getXp() {
        return xp;
    }

    public static void setXp(int xp) {
        ClientDataStorage.xp = xp;
    }

    public static int getAvailablePoints() {
        return availablePoints;
    }

    public static void setAvailablePoints(int availablePoints) {
        ClientDataStorage.availablePoints = availablePoints;
    }

    public static int getRequiredXp() {
        return requiredXp;
    }

    public static void setRequiredXp(int requiredXp) {
        ClientDataStorage.requiredXp = requiredXp;
    }

    public static String getPrimaryAbility() {
        return primaryAbility;
    }

    public static void setPrimaryAbility(String primaryAbility) {
        ClientDataStorage.primaryAbility = primaryAbility;
    }

    public static String getSecondaryAbility() {
        return secondaryAbility;
    }

    public static void setSecondaryAbility(String secondaryAbility) {
        ClientDataStorage.secondaryAbility = secondaryAbility;
    }
}

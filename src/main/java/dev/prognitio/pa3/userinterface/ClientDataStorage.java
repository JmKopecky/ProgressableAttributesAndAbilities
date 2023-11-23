package dev.prognitio.pa3.userinterface;

public class ClientDataStorage {

    private static int level;
    private static int xp;
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
        return -1;
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
}

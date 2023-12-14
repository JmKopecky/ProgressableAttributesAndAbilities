package dev.prognitio.pa3.capabililty;

import dev.prognitio.pa3.AttributeType;
import dev.prognitio.pa3.ForgeModNetworking;
import dev.prognitio.pa3.userhud.SyncCooldownDataSC;
import dev.prognitio.pa3.userinterface.packets.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.function.Supplier;

public class AttributesCapability {

    private int experience = 0;
    private int level = 0;
    private int availablePoints = 0;
    private int abilityCooldown = 0;
    private int currentMaxCooldown = 0;
    private int passiveDodgeProc = 0;
    private int passiveParryProc = 0;
    private int passiveDoubleStrikeProc = 0;

    Supplier<ArrayList<Double>> fitnessSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(2.0);
        return list;
    };
    public AttributeType fitness = new AttributeType("fitness", 10,
            "5c93580c-84a1-4583-83a9-3c29ca3abb3e", fitnessSupp.get(), 1);

    Supplier<ArrayList<Double>> resilienceSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(2.0);
        list.add(1.0);
        return list;
    };
    public AttributeType resilience = new AttributeType("resilience", 10,
            "e19e60ec-c913-433b-b38a-3801834be5cf", resilienceSupp.get(), 1);

    Supplier<ArrayList<Double>> combatSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.5);
        list.add(5.0); //parry amount per level, start at 25, max at 75
        return list;
    };
    public AttributeType combat = new AttributeType("combat", 10,
            "b7b13584-e7d3-4dea-9d09-f6c36b9bef2c", combatSupp.get(), 1);

    Supplier<ArrayList<Double>> nimblenessSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.1);
        list.add(3.0); //dodge chance scaling
        return list;
    };
    public AttributeType nimbleness = new AttributeType("nimbleness", 10,
            "db7daa72-f2e0-40b0-8120-5816bf0ba274", nimblenessSupp.get(), 1);

    Supplier<ArrayList<Double>> strategySupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.5);
        list.add(4.0);
        return list;
    };
    public AttributeType strategy = new AttributeType("strategy", 10,
            "5f14399f-df13-4007-bd76-7f5cbad40f9f", strategySupp.get(), 1);

    //abilities
    public AbilityType dash = new AbilityType("dash", 5, 6, -1, 2, 1);
    public AbilityType arrowSalvo = new AbilityType("arrowsalvo", 5, 5, +1, 3, 2);
    public AbilityType overshield = new AbilityType("overshield", 5, 16, +2, 3, 2);
    public AbilityType incendiaryLance = new AbilityType("incendiarylance", 5, 15, -1, 3, 1);
    public AbilityType chainLightning = new AbilityType("chainlightning", 5, 15, -1, 3, 2);
    public AbilityType deflectiveShield = new AbilityType("deflectiveshield", 5, 8, -1, 2, 1);


    String primaryAbility = "dash";
    String secondaryAbility = "arrowsalvo";
    String lastTriggered = "empty";

    public void applyApplicableAttributes(Player player) {
        //fitness
        AttributeModifier fitnessModifier = fitness.buildModifier(0, null);
        if (player.getAttribute(Attributes.MAX_HEALTH).hasModifier(fitnessModifier)) {
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(fitnessModifier);
        }
        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(fitnessModifier);

        //resilience
        AttributeModifier resilienceModifier = resilience.buildModifier(0, null);
        if (player.getAttribute(Attributes.ARMOR).hasModifier(resilienceModifier)) {
            player.getAttribute(Attributes.ARMOR).removeModifier(resilienceModifier);
        }
        player.getAttribute(Attributes.ARMOR).addTransientModifier(resilienceModifier);
        AttributeModifier resilienceToughnessModifier = resilience.buildModifier(1,
                "74d66138-0f55-459c-95de-2c29560a0000");
        if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(resilienceToughnessModifier)) {
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(resilienceToughnessModifier);
        }
        player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(resilienceToughnessModifier);

        //combat
        AttributeModifier combatModifier = combat.buildModifier(0, null);
        if (player.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(combatModifier)) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(combatModifier);
        }
        player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(combatModifier);

        //nimbleness
        AttributeModifier nimbleModifier = nimbleness.buildModifier(0, null);
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(nimbleModifier)) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(nimbleModifier);
        }
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(nimbleModifier);

        //strategy
        AttributeModifier strategyModifier = strategy.buildModifier(0, null);
        if (player.getAttribute(ForgeMod.REACH_DISTANCE.get()).hasModifier(strategyModifier)) {
            player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(strategyModifier);
        }
        player.getAttribute(ForgeMod.REACH_DISTANCE.get()).addTransientModifier(strategyModifier);
        AttributeModifier strategyAttackReachModifier = strategy.buildModifier(0, "a218bc25-dbbf-40e4-9a77-e3befddb7846");
        if (player.getAttribute(ForgeMod.ATTACK_RANGE.get()).hasModifier(strategyAttackReachModifier)) {
            player.getAttribute(ForgeMod.ATTACK_RANGE.get()).removeModifier(strategyAttackReachModifier);
        }
        player.getAttribute(ForgeMod.ATTACK_RANGE.get()).addTransientModifier(strategyAttackReachModifier);

    }

    public void syncDataToPlayer(Player player) {
        String lvl = "fitness:" + fitness.level;
        String mxLvl = "fitness:" + fitness.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "resilience:" + resilience.level;
        mxLvl = "resilience:" + resilience.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "combat:" + combat.level;
        mxLvl = "combat:" + combat.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "nimbleness:" + nimbleness.level;
        mxLvl = "nimbleness:" + nimbleness.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "strategy:" + strategy.level;
        mxLvl = "strategy:" + strategy.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);

        ForgeModNetworking.sendToPlayer(new SyncPointsSC("" + availablePoints), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncTotalLevelSC("" + level), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncXPSC("" + experience), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncXpLevelReqSC("" + calculateLevelUpRequirement()), (ServerPlayer) player);

        lvl = "dash:" + dash.level;
        mxLvl = "dash:" + dash.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "arrowsalvo:" + arrowSalvo.level;
        mxLvl = "arrowsalvo:" + arrowSalvo.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "overshield:" + overshield.level;
        mxLvl = "overshield:" + overshield.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "incendiarylance:" + incendiaryLance.level;
        mxLvl = "incendiarylance:" + incendiaryLance.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "chainlightning:" + chainLightning.level;
        mxLvl = "chainlightning:" + chainLightning.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "deflectiveshield:" + deflectiveShield.level;
        mxLvl = "deflectiveshield:" + deflectiveShield.maxLevel;
        ForgeModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ForgeModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        ForgeModNetworking.sendToPlayer(new SyncSelectedAbililtySC(primaryAbility + ":" + secondaryAbility), (ServerPlayer) player);

        int pointsToUpgrade = 1 + dash.level / 2 * dash.upgradeScale;
        if (dash.level == 0) {
            pointsToUpgrade = dash.purchaseCost;
        }
        ForgeModNetworking.sendToPlayer(new SyncAbilUpgCostSC("dash:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + arrowSalvo.level / 2 * arrowSalvo.upgradeScale;
        if (arrowSalvo.level == 0) {
            pointsToUpgrade = arrowSalvo.purchaseCost;
        }
        ForgeModNetworking.sendToPlayer(new SyncAbilUpgCostSC("arrowsalvo:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + overshield.level / 2 * overshield.upgradeScale;
        if (overshield.level == 0) {
            pointsToUpgrade = overshield.purchaseCost;
        }
        ForgeModNetworking.sendToPlayer(new SyncAbilUpgCostSC("overshield:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + incendiaryLance.level / 2 * incendiaryLance.upgradeScale;
        if (incendiaryLance.level == 0) {
            pointsToUpgrade = incendiaryLance.purchaseCost;
        }
        ForgeModNetworking.sendToPlayer(new SyncAbilUpgCostSC("incendiarylance:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + chainLightning.level / 2 * chainLightning.upgradeScale;
        if (chainLightning.level == 0) {
            pointsToUpgrade = chainLightning.purchaseCost;
        }
        ForgeModNetworking.sendToPlayer(new SyncAbilUpgCostSC("chainlightning:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + deflectiveShield.level / 2 * deflectiveShield.upgradeScale;
        if (deflectiveShield.level == 0) {
            pointsToUpgrade = deflectiveShield.purchaseCost;
        }
        ForgeModNetworking.sendToPlayer(new SyncAbilUpgCostSC("deflectiveshield:" + pointsToUpgrade), (ServerPlayer) player);


        pointsToUpgrade = (int) ((fitness.level/5 + 1) * fitness.pointRequirementScale);
        ForgeModNetworking.sendToPlayer(new SyncAttrUpgCostSC("fitness:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((resilience.level/5 + 1) * resilience.pointRequirementScale);
        ForgeModNetworking.sendToPlayer(new SyncAttrUpgCostSC("resilience:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((combat.level/5 + 1) * combat.pointRequirementScale);
        ForgeModNetworking.sendToPlayer(new SyncAttrUpgCostSC("combat:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((strategy.level/5 + 1) * strategy.pointRequirementScale);
        ForgeModNetworking.sendToPlayer(new SyncAttrUpgCostSC("strategy:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((nimbleness.level/5 + 1) * nimbleness.pointRequirementScale);
        ForgeModNetworking.sendToPlayer(new SyncAttrUpgCostSC("nimbleness:" + pointsToUpgrade), (ServerPlayer) player);

        int isElite = dash.isEliteVersion ? 1 : 0;
        ForgeModNetworking.sendToPlayer(new SyncAbilEliteSC("dash:" + isElite), (ServerPlayer) player);

        isElite = arrowSalvo.isEliteVersion ? 1 : 0;
        ForgeModNetworking.sendToPlayer(new SyncAbilEliteSC("arrowsalvo:" + isElite), (ServerPlayer) player);

        isElite = overshield.isEliteVersion ? 1 : 0;
        ForgeModNetworking.sendToPlayer(new SyncAbilEliteSC("overshield:" + isElite), (ServerPlayer) player);

        isElite = incendiaryLance.isEliteVersion ? 1 : 0;
        ForgeModNetworking.sendToPlayer(new SyncAbilEliteSC("incendiarylance:" + isElite), (ServerPlayer) player);

        isElite = chainLightning.isEliteVersion ? 1 : 0;
        ForgeModNetworking.sendToPlayer(new SyncAbilEliteSC("chainlightning:" + isElite), (ServerPlayer) player);

        isElite = deflectiveShield.isEliteVersion ? 1 : 0;
        ForgeModNetworking.sendToPlayer(new SyncAbilEliteSC("deflectiveshield:" + isElite), (ServerPlayer) player);

        ForgeModNetworking.sendToPlayer(new SyncCooldownDataSC(abilityCooldown + ":" + currentMaxCooldown), (ServerPlayer) player);
    }

    public boolean attemptLevelUpAttribute(String attribute) {
        if (availablePoints <= 0) { //if no attribute points are available
            return false;
        }
        switch (attribute) {
            case "fitness" -> {
                int result = fitness.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            case "resilience" -> {
                int result = resilience.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            case "combat" -> {
                int result = combat.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            case "nimbleness" -> {
                int result = nimbleness.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            case "strategy" -> {
                int result = strategy.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    public void attemptUnlockElite(String ability) {
        switch (ability) {
            case "dash" -> {
                int result = dash.attemptUnlockElite(availablePoints);
                if (availablePoints - result <= availablePoints) {
                    setAvailablePoints(availablePoints - result);
                }
            }
            case "arrowsalvo" -> {
                int result = arrowSalvo.attemptUnlockElite(availablePoints);
                if (availablePoints - result <= availablePoints) {
                    setAvailablePoints(availablePoints - result);
                }
            }
            case "overshield" -> {
                int result = overshield.attemptUnlockElite(availablePoints);
                if (availablePoints - result <= availablePoints) {
                    setAvailablePoints(availablePoints - result);
                }
            }
            case "incendiarylance" -> {
                int result = incendiaryLance.attemptUnlockElite(availablePoints);
                if (availablePoints - result <= availablePoints) {
                    setAvailablePoints(availablePoints - result);
                }
            }
            case "chainlightning" -> {
                int result = chainLightning.attemptUnlockElite(availablePoints);
                if (availablePoints - result <= availablePoints) {
                    setAvailablePoints(availablePoints - result);
                }
            }
            case "deflectiveshield" -> {
                int result = deflectiveShield.attemptUnlockElite(availablePoints);
                if (availablePoints - result <= availablePoints) {
                    setAvailablePoints(availablePoints - result);
                }
            }
        }
    }

    public void setPrimaryAbility(String primaryAbility) {
        this.primaryAbility = primaryAbility;
    }

    public void setSecondaryAbility(String secondaryAbility) {
        this.secondaryAbility = secondaryAbility;
    }

    public void firePrimaryAbility(Player player) {
        if (abilityCooldown <= 0) {
            getAbilityFromString(primaryAbility).runAbility(player);
            setLastTriggered(primaryAbility);
        }
    }

    public void fireSecondaryAbility(Player player) {
        if (abilityCooldown <= 0) {
            getAbilityFromString(secondaryAbility).runAbility(player);
            setLastTriggered(secondaryAbility);
        }
    }

    public AbilityType getAbilityFromString(String input) {
        switch (input) {
            case "dash" -> {return dash;}
            case "arrowsalvo" -> {return arrowSalvo;}
            case "overshield" -> {return overshield;}
            case "incendiarylance" -> {return incendiaryLance;}
            case "chainlightning" -> {return chainLightning;}
            case "deflectiveshield" -> {return deflectiveShield;}
        }
        return null;
    }

    public int calculateLevelUpRequirement() {
        //for each level, make it 2% harder to get another level
        int initialXPRequirement = 1000; //what should the base xp requirement be to go from level 0 to level 1
        float perLevelScaling = 1.02f; //how much the exp requirement should increase each level.
        return (int) (initialXPRequirement * (Math.pow(perLevelScaling, level)));
    }

    public void addXP(int experience) {
        //if experience is not enough to level up, add to experience. else, level up, zero out experience, add the amount extra after leveling up to experience
        int levelUpDistance = calculateLevelUpRequirement() - this.experience;
        if (levelUpDistance > experience) {
            //add xp, don't level up
            this.experience += experience;
        } else {
            level++;
            availablePoints++;
            this.experience = experience - levelUpDistance;
            while (this.experience > calculateLevelUpRequirement()) {
                this.experience -= calculateLevelUpRequirement();
                level++;
                availablePoints++;
            }

        }
    }

    //handle the data
    public void copyFrom(AttributesCapability source) {
        this.experience = source.experience;
        this.level = source.level;
        this.availablePoints = source.availablePoints;
        this.combat = source.combat;
        this.fitness = source.fitness;
        this.resilience = source.resilience;
        this.nimbleness = source.nimbleness;
        this.strategy = source.strategy;
        this.abilityCooldown = source.abilityCooldown;
        this.currentMaxCooldown = source.currentMaxCooldown;
        this.passiveDodgeProc = source.passiveDodgeProc;
        this.passiveParryProc = source.passiveParryProc;
        this.passiveDoubleStrikeProc = source.passiveDoubleStrikeProc;
        this.primaryAbility = source.primaryAbility;
        this.secondaryAbility = source.secondaryAbility;
        this.dash = source.dash;
        this.arrowSalvo = source.arrowSalvo;
        this.overshield = source.overshield;
        this.incendiaryLance = source.incendiaryLance;
        this.chainLightning = source.chainLightning;
        this.deflectiveShield = source.deflectiveShield;
        this.lastTriggered = source.lastTriggered;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("experience", this.experience);
        nbt.putInt("level", this.level);
        nbt.putInt("availablepoints", this.availablePoints);
        nbt.putString("lastfired", this.lastTriggered);
        nbt.putString("fitness", fitness.toString());
        nbt.putString("resilience", resilience.toString());
        nbt.putString("combat", combat.toString());
        nbt.putString("nimbleness", nimbleness.toString());
        nbt.putString("strategy", strategy.toString());
        nbt.putInt("cooldown", abilityCooldown);
        nbt.putInt("maxcooldown", currentMaxCooldown);
        nbt.putInt("dodgeproc", passiveDodgeProc);
        nbt.putInt("parryproc", passiveParryProc);
        nbt.putInt("doublestrikeproc", passiveDoubleStrikeProc);
        if (dash != null) {
            nbt.putString("dash", dash.toString());
        }
        if (arrowSalvo != null) {
            nbt.putString("arrowsalvo", arrowSalvo.toString());
        }
        if (overshield != null) {
            nbt.putString("overshield", overshield.toString());
        }
        if (incendiaryLance != null) {
            nbt.putString("incendiarylance", incendiaryLance.toString());
        }
        if (chainLightning != null) {
            nbt.putString("chainlightning", chainLightning.toString());
        }
        if (deflectiveShield != null) {
            nbt.putString("deflectiveshield", deflectiveShield.toString());
        }
        nbt.putString("primaryability", primaryAbility);
        nbt.putString("secondaryability", secondaryAbility);

    }

    public void loadNBTData(CompoundTag nbt) {
        this.experience = nbt.getInt("experience");
        this.level = nbt.getInt("level");
        this.availablePoints = nbt.getInt("availablepoints");
        this.lastTriggered = nbt.getString("lastfired");
        this.fitness = AttributeType.fromString(nbt.getString("fitness"));
        this.combat = AttributeType.fromString(nbt.getString("combat"));
        this.resilience = AttributeType.fromString(nbt.getString("resilience"));
        this.nimbleness = AttributeType.fromString(nbt.getString("nimbleness"));
        this.strategy = AttributeType.fromString(nbt.getString("strategy"));
        this.abilityCooldown = nbt.getInt("cooldown");
        this.currentMaxCooldown = nbt.getInt("maxcooldown");
        this.passiveDodgeProc = nbt.getInt("dodgeproc");
        this.passiveParryProc = nbt.getInt("parryproc");
        this.passiveDoubleStrikeProc = nbt.getInt("doublestrikeproc");
        String primary = nbt.getString("primaryability");
        if (primary.equals("")) {
            primaryAbility = "dash";
        } else {
            primaryAbility = primary;
        }
        String secondary = nbt.getString("secondaryability");
        if (secondary.equals("")) {
            secondaryAbility = "arrowsalvo";
        } else {
            secondaryAbility = secondary;
        }
        String dashString = nbt.getString("dash");
        if (!dashString.equals("")) {
            this.dash = AbilityType.fromString(dashString);
        }
        String arrowSalvoString = nbt.getString("arrowsalvo");
        if (!arrowSalvoString.equals("")) {
            this.arrowSalvo = AbilityType.fromString(arrowSalvoString);
        }
        String overshieldString = nbt.getString("overshield");
        if (!overshieldString.equals("")) {
            this.overshield = AbilityType.fromString(overshieldString);
        }
        String incendiaryLanceString = nbt.getString("incendiarylance");
        if (!incendiaryLanceString.equals("")) {
            this.incendiaryLance = AbilityType.fromString(incendiaryLanceString);
        }
        String chainLightningString = nbt.getString("chainlightning");
        if (!chainLightningString.equals("")) {
            this.chainLightning = AbilityType.fromString(chainLightningString);
        }
        String deflectiveShieldString = nbt.getString("deflectiveshield");
        if (!deflectiveShieldString.equals("")) {
            this.deflectiveShield = AbilityType.fromString(deflectiveShieldString);
        }
    }

    public String getLastTriggered() {
        return lastTriggered;
    }

    public void setLastTriggered(String triggered) {
        this.lastTriggered = triggered;
    }

    //getter and setter methods
    public int getExperience() {
        return this.experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }
    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = Math.max(availablePoints, 0);
    }

    public int getAbilityCooldown() { return abilityCooldown; }
    public void setAbilityCooldown(int abilityCooldown) { this.abilityCooldown = abilityCooldown; }

    public int getCurrentMaxCooldown() {
        return currentMaxCooldown;
    }

    public void setCurrentMaxCooldown(int currentMaxCooldown) {
        this.currentMaxCooldown = currentMaxCooldown;
    }

    public void triggerDodgeProc() {
        this.passiveDodgeProc = 40;
    }

    public void triggerParryProc() {
        this.passiveParryProc = 40;
    }

    public void triggerDoubleStrikeProc() {
        this.passiveDoubleStrikeProc = 40;
    }

    public void decreasePassiveTimers() {
        if (passiveDodgeProc > 0) {
            this.passiveDodgeProc--;
        }
        if (passiveParryProc > 0) {
            this.passiveParryProc--;
        }
        if (passiveDoubleStrikeProc > 0) {
            this.passiveDoubleStrikeProc--;
        }
    }

    public int getPassiveDodgeProc() {
        return passiveDodgeProc;
    }
    public int getPassiveParryProc() {
        return passiveParryProc;
    }
    public int getPassiveDoubleStrikeProc() {
        return passiveDoubleStrikeProc;
    }
}

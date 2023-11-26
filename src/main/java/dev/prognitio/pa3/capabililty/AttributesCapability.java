package dev.prognitio.pa3.capabililty;

import dev.prognitio.pa3.ModNetworking;
import dev.prognitio.pa3.userhud.SyncCooldownDataSC;
import dev.prognitio.pa3.userinterface.packets.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import org.apache.logging.log4j.core.jmx.Server;

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
        list.add(1.0);
        return list;
    };
    public AttributeType fitness = new AttributeType("fitness", 20,
            "5c93580c-84a1-4583-83a9-3c29ca3abb3e", fitnessSupp.get(), 1);

    Supplier<ArrayList<Double>> resilienceSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(1.0);
        return list;
    };
    public AttributeType resilience = new AttributeType("resilience", 20,
            "e19e60ec-c913-433b-b38a-3801834be5cf", resilienceSupp.get(), 1);

    Supplier<ArrayList<Double>> combatSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.5);
        list.add(2.5); //parry amount per level, start at 25, max at 75
        return list;
    };
    public AttributeType combat = new AttributeType("combat", 20,
            "b7b13584-e7d3-4dea-9d09-f6c36b9bef2c", combatSupp.get(), 1);

    Supplier<ArrayList<Double>> nimblenessSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.05);
        list.add(1.5); //dodge chance scaling
        return list;
    };
    public AttributeType nimbleness = new AttributeType("nimbleness", 20,
            "db7daa72-f2e0-40b0-8120-5816bf0ba274", nimblenessSupp.get(), 1);

    Supplier<ArrayList<Double>> strategySupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.25);
        list.add(2.0);
        return list;
    };
    public AttributeType strategy = new AttributeType("strategy", 20,
            "5f14399f-df13-4007-bd76-7f5cbad40f9f", strategySupp.get(), 1);

    //abilities
    public AbilityType dash = new AbilityType("dash", 5, 6, -1, 3, 1);
    public AbilityType arrowSalvo = new AbilityType("arrowsalvo", 5, 5, +1, 4, 2);
    public AbilityType overshield = new AbilityType("overshield", 5, 16, +2, 4, 2);

    String primaryAbility = "dash";
    String secondaryAbility = "arrowsalvo";

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
        AttributeModifier combatSpeedModifier = combat.buildModifier(1,
                "a0b4fa02-421d-4332-995f-974cc51f7378");
        if (player.getAttribute(Attributes.ATTACK_SPEED).hasModifier(combatSpeedModifier)) {
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(combatSpeedModifier);
        }
        player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(combatSpeedModifier);

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
        ModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "resilience:" + resilience.level;
        mxLvl = "resilience:" + resilience.maxLevel;
        ModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "combat:" + combat.level;
        mxLvl = "combat:" + combat.maxLevel;
        ModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "nimbleness:" + nimbleness.level;
        mxLvl = "nimbleness:" + nimbleness.maxLevel;
        ModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);
        lvl = "strategy:" + strategy.level;
        mxLvl = "strategy:" + strategy.maxLevel;
        ModNetworking.sendToPlayer(new SyncAttrLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAttrMaxLevelSC(mxLvl), (ServerPlayer) player);

        ModNetworking.sendToPlayer(new SyncPointsSC("" + availablePoints), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncTotalLevelSC("" + level), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncXPSC("" + experience), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncXpLevelReqSC("" + calculateLevelUpRequirement()), (ServerPlayer) player);

        lvl = "dash:" + dash.level;
        mxLvl = "dash:" + dash.maxLevel;
        ModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "arrowsalvo:" + arrowSalvo.level;
        mxLvl = "arrowsalvo:" + arrowSalvo.maxLevel;
        ModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        lvl = "overshield:" + overshield.level;
        mxLvl = "overshield:" + overshield.maxLevel;
        ModNetworking.sendToPlayer(new SyncAbilLevelSC(lvl), (ServerPlayer) player);
        ModNetworking.sendToPlayer(new SyncAbilMaxLevelSC(mxLvl), (ServerPlayer) player);

        ModNetworking.sendToPlayer(new SyncSelectedAbililtySC(primaryAbility + ":" + secondaryAbility), (ServerPlayer) player);

        int pointsToUpgrade = 1 + dash.level * dash.upgradeScale;
        if (dash.level == 0) {
            pointsToUpgrade = dash.purchaseCost;
        }
        ModNetworking.sendToPlayer(new SyncAbilUpgCostSC("dash:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + arrowSalvo.level * arrowSalvo.upgradeScale;
        if (arrowSalvo.level == 0) {
            pointsToUpgrade = arrowSalvo.purchaseCost;
        }
        ModNetworking.sendToPlayer(new SyncAbilUpgCostSC("arrowsalvo:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = 1 + overshield.level * overshield.upgradeScale;
        if (overshield.level == 0) {
            pointsToUpgrade = overshield.purchaseCost;
        }
        ModNetworking.sendToPlayer(new SyncAbilUpgCostSC("overshield:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((fitness.level/5 + 1) * fitness.pointRequirementScale);
        ModNetworking.sendToPlayer(new SyncAttrUpgCostSC("fitness:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((resilience.level/5 + 1) * resilience.pointRequirementScale);
        ModNetworking.sendToPlayer(new SyncAttrUpgCostSC("resilience:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((combat.level/5 + 1) * combat.pointRequirementScale);
        ModNetworking.sendToPlayer(new SyncAttrUpgCostSC("combat:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((strategy.level/5 + 1) * strategy.pointRequirementScale);
        ModNetworking.sendToPlayer(new SyncAttrUpgCostSC("strategy:" + pointsToUpgrade), (ServerPlayer) player);

        pointsToUpgrade = (int) ((nimbleness.level/5 + 1) * nimbleness.pointRequirementScale);
        ModNetworking.sendToPlayer(new SyncAttrUpgCostSC("nimbleness:" + pointsToUpgrade), (ServerPlayer) player);

        int isElite = dash.isEliteVersion ? 1 : 0;
        ModNetworking.sendToPlayer(new SyncAbilEliteSC("dash:" + isElite), (ServerPlayer) player);

        isElite = arrowSalvo.isEliteVersion ? 1 : 0;
        ModNetworking.sendToPlayer(new SyncAbilEliteSC("arrowsalvo:" + isElite), (ServerPlayer) player);

        isElite = overshield.isEliteVersion ? 1 : 0;
        ModNetworking.sendToPlayer(new SyncAbilEliteSC("overshield:" + isElite), (ServerPlayer) player);

        ModNetworking.sendToPlayer(new SyncCooldownDataSC(abilityCooldown + ":" + currentMaxCooldown), (ServerPlayer) player);
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

    public boolean attemptUnlockElite(String ability) {
        switch (ability) {
            case "dash" -> {
                return dash.attemptUnlockElite(availablePoints) != -1;
            }
            case "arrowsalvo" -> {
                return arrowSalvo.attemptUnlockElite(availablePoints) != -1;
            }
            case "overshield" -> {
                return overshield.attemptUnlockElite(availablePoints) != -1;
            }
        }
        return false;
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
        }
    }

    public void fireSecondaryAbility(Player player) {
        if (abilityCooldown <= 0) {
            getAbilityFromString(secondaryAbility).runAbility(player);
        }
    }

    public AbilityType getAbilityFromString(String input) {
        switch (input) {
            case "dash" -> {return dash;}
            case "arrowsalvo" -> {return arrowSalvo;}
            case "overshield" -> {return overshield;}
        }
        return null;
    }

    public int calculateLevelUpRequirement() {
        //for each level, make it 10% harder to get another level
        int initialXPRequirement = 10000; //what should the base xp requirement be to go from level 0 to level 1
        float perLevelScaling = 1.05f; //how much the exp requirement should increase each level.
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
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("experience", this.experience);
        nbt.putInt("level", this.level);
        nbt.putInt("availablepoints", this.availablePoints);
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
        nbt.putString("dash", dash.toString());
        nbt.putString("arrowsalvo", arrowSalvo.toString());
        nbt.putString("overshield", overshield.toString());
        nbt.putString("primaryability", primaryAbility);
        nbt.putString("secondaryability", secondaryAbility);

    }

    public void loadNBTData(CompoundTag nbt) {
        this.experience = nbt.getInt("experience");
        this.level = nbt.getInt("level");
        this.availablePoints = nbt.getInt("availablepoints");
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
        this.primaryAbility = nbt.getString("primaryability");
        this.secondaryAbility = nbt.getString("secondaryability");
        this.dash = AbilityType.fromString(nbt.getString("dash"));
        this.arrowSalvo = AbilityType.fromString(nbt.getString("arrowsalvo"));
        this.overshield = AbilityType.fromString(nbt.getString("overshield"));
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
        this.availablePoints = availablePoints;
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

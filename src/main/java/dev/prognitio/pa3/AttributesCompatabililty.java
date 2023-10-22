package dev.prognitio.pa3;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.function.Supplier;

public class AttributesCompatabililty {

    private int experience = 0;
    private int level = 0;
    private int availablePoints = 0;

    Supplier<ArrayList<Double>> fitnessSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(1.0);
        return list;
    };
    AttributeType fitness = new AttributeType("fitness", 20,
            "5c93580c-84a1-4583-83a9-3c29ca3abb3e", fitnessSupp.get(), 1);

    Supplier<ArrayList<Double>> resilienceSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(1.0);
        return list;
    };
    AttributeType resilience = new AttributeType("resilience", 20,
            "e19e60ec-c913-433b-b38a-3801834be5cf", resilienceSupp.get(), 1);

    Supplier<ArrayList<Double>> combatSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.5);
        list.add(50.0/20.0); //parry amount per level, start at 25, max at 75
        return list;
    };
    AttributeType combat = new AttributeType("combat", 20,
            "b7b13584-e7d3-4dea-9d09-f6c36b9bef2c", combatSupp.get(), 1);

    Supplier<ArrayList<Double>> nimblenessSupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.05);
        list.add(50.0/20.0);
        return list;
    };
    AttributeType nimbleness = new AttributeType("nimbleness", 20,
            "db7daa72-f2e0-40b0-8120-5816bf0ba274", nimblenessSupp.get(), 1);

    Supplier<ArrayList<Double>> strategySupp = () -> {
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.25);
        list.add(2.0);
        return list;
    };
    AttributeType strategy = new AttributeType("strategy", 20,
            "5f14399f-df13-4007-bd76-7f5cbad40f9f", strategySupp.get(), 2);

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

    public int calculateLevelUpRequirement() {
        //for each level, make it 10% harder to get another level
        int initialXPRequirement = 1000; //what should the base xp requirement be to go from level 0 to level 1
        float perLevelScaling = 1.1f; //how much the exp requirement should increase each level.
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
    public void copyFrom(AttributesCompatabililty source) {
        this.experience = source.experience;
        this.level = source.level;
        this.availablePoints = source.availablePoints;
        this.combat = source.combat;
        this.fitness = source.fitness;
        this.resilience = source.resilience;
        this.nimbleness = source.nimbleness;
        this.strategy = source.strategy;
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

}

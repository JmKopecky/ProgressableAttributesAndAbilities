package dev.prognitio.pa3;

import net.minecraft.nbt.CompoundTag;

public class Attributes {

    private double experience = 0f;
    private int level = 0;
    private int availablePoints = 0;
    private int healthLevel = 0;
    private int toughnessLevel = 0;
    private int strengthLevel = 0;

    public boolean attemptLevelUpAttribute(String attribute) {
        if (availablePoints <= 0) { //if no attribute points are available
            return false;
        }
        switch (attribute) {
            case "health" -> {
                int skillPointReq = (healthLevel / 5) + 1;
                if (availablePoints >= skillPointReq) {
                    healthLevel++;
                    availablePoints -= skillPointReq;
                } else {
                    return false;
                }
            }
            case "toughness" -> {
                int skillPointReq = (toughnessLevel / 5) + 1;
                if (availablePoints >= skillPointReq) {
                    toughnessLevel++;
                    availablePoints -= skillPointReq;
                } else {
                    return false;
                }
            }
            case "strength" -> {
                int skillPointReq = (strengthLevel / 5) + 1;
                if (availablePoints >= skillPointReq) {
                    strengthLevel++;
                    availablePoints -= skillPointReq;
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    public double calculateLevelUpRequirement() {
        //for each level, make it 10% harder to get another level
        int initialXPRequirement = 1000; //what should the base xp requirement be to go from level 0 to level 1
        float perLevelScaling = 1.1f; //how much the exp requirement should increase each level.
        return initialXPRequirement * (Math.pow(perLevelScaling, level));
    }

    public void addXP(double experience) {
        //if experience is not enough to level up, add to experience. else, level up, zero out experience, add the amount extra after leveling up to experience
        double levelUpDistance = calculateLevelUpRequirement() - this.experience;
        if (levelUpDistance > experience) {
            //add xp, don't level up
            this.experience += experience;
        } else {
            level++;
            this.experience = experience - levelUpDistance;
        }
    }

    //handle the data
    public void copyFrom(Attributes source) {
        this.experience = source.experience;
        this.level = source.level;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("experience", this.experience);
        nbt.putInt("level", this.level);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.experience = nbt.getDouble("experience");
        this.level = nbt.getInt("level");
    }

    //getter and setter methods
    public double getExperience() {
        return this.experience;
    }
    public void setExperience(float experience) {
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

    public int getHealthLevel() {
        return healthLevel;
    }
    public void setHealthLevel(int healthLevel) {
        this.healthLevel = healthLevel;
    }

    public int getToughnessLevel() {
        return toughnessLevel;
    }
    public void setToughnessLevel(int toughnessLevel) {
        this.toughnessLevel = toughnessLevel;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }
    public void setStrengthLevel(int strengthLevel) {
        this.strengthLevel = strengthLevel;
    }
}

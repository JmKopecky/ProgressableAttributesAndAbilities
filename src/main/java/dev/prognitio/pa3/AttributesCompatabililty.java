package dev.prognitio.pa3;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class AttributesCompatabililty {

    private int experience = 0;
    private int level = 0;
    private int availablePoints = 0;
    AttributeType health = new AttributeType("health", 20,
            "5c93580c-84a1-4583-83a9-3c29ca3abb3e", 1.0);
    AttributeType toughness = new AttributeType("toughness", 20,
            "e19e60ec-c913-433b-b38a-3801834be5cf", 1.0);
    AttributeType strength = new AttributeType("strength", 20,
            "b7b13584-e7d3-4dea-9d09-f6c36b9bef2c", 1.0);

    public void applyApplicableAttributes(Player player) {
        if (player == null) {return;}
        AttributeModifier healthModifier = health.buildModifier();
        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthModifier)) {
            player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthModifier);
        }
        AttributeModifier toughnessModifier = toughness.buildModifier();
        if (!player.getAttribute(Attributes.ARMOR).hasModifier(toughnessModifier)) {
            player.getAttribute(Attributes.ARMOR).addTransientModifier(toughnessModifier);
        }
        AttributeModifier strengthModifier = strength.buildModifier();
        if (!player.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(strengthModifier)) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(strengthModifier);
        }
    }

    public boolean attemptLevelUpAttribute(String attribute) {
        if (availablePoints <= 0) { //if no attribute points are available
            return false;
        }
        switch (attribute) {
            case "health" -> {
                int result = health.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            case "toughness" -> {
                int result = toughness.attemptLevelUp(availablePoints);
                if (result == -1) {
                    return false;
                } else {
                    availablePoints -= result;
                }
            }
            case "strength" -> {
                int result = strength.attemptLevelUp(availablePoints);
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
        this.strength = source.strength;
        this.health = source.health;
        this.toughness = source.toughness;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("experience", this.experience);
        nbt.putInt("level", this.level);
        nbt.putString("health", health.toString());
        nbt.putString("toughness", toughness.toString());
        nbt.putString("strength", strength.toString());
    }

    public void loadNBTData(CompoundTag nbt) {
        this.experience = nbt.getInt("experience");
        this.level = nbt.getInt("level");
        this.health = AttributeType.fromString(nbt.getString("health"));
        this.strength = AttributeType.fromString(nbt.getString("strength"));
        this.toughness = AttributeType.fromString(nbt.getString("toughness"));
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

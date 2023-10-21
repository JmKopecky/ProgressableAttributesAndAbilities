package dev.prognitio.pa3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeType {

    String id;
    String uuid;
    int maxLevel;
    int level;
    Double perLevelScaleValue;

    public AttributeType(String id, int maxLevel, String uuid, Double perLevelScaleValue) {
        this.id = id;
        this.uuid = uuid;
        this.maxLevel = maxLevel;
        this.perLevelScaleValue = perLevelScaleValue;
        this.level = 5;
    }

    public int attemptLevelUp(int availablePoints) { //output is points to deduct, -1 if not enough points
        int requiredPoints = (level/5) + 1;
        if (level != maxLevel && availablePoints >= requiredPoints) {
            level++;
            return requiredPoints;
        } else {
            return -1;
        }
    }

    public double calculatePower() {
        return level * perLevelScaleValue;
    }

    public AttributeModifier buildModifier() {
        return new AttributeModifier(UUID.fromString(uuid), id, calculatePower(), AttributeModifier.Operation.ADDITION);
    }

    public static AttributeType fromString(String GSON) {
        return ((new GsonBuilder()).create()).fromJson(GSON, AttributeType.class);
    }

    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}

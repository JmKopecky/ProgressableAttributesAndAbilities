package dev.prognitio.pa3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public class AttributeType {

    String id;
    String uuid;
    int maxLevel;
    int level;
    ArrayList<Double> perLevelScaleValue;
    double pointRequirementScale;

    public AttributeType(String id, int maxLevel, String uuid, ArrayList<Double> perLevelScaleValue, double pointRequirementScale) {
        this.id = id;
        this.uuid = uuid;
        this.maxLevel = maxLevel;
        this.perLevelScaleValue = perLevelScaleValue;
        this.level = 0;
        this.pointRequirementScale = pointRequirementScale;
    }

    public int attemptLevelUp(int availablePoints) { //output is points to deduct, -1 if not enough points
        int requiredPoints = (int) (((level/5) + 1) * pointRequirementScale);
        if (level != maxLevel && availablePoints >= requiredPoints) {
            level++;
            return requiredPoints;
        } else {
            return -1;
        }
    }

    public double calculatePower(int index) {
        return level * perLevelScaleValue.get(index);
    }

    public AttributeModifier buildModifier(int index, @Nullable String extraUUID) {
        String usedUUID = uuid;
        if (extraUUID != null) {
            usedUUID = extraUUID;
        }
        if (id.equals("nimbleness") && index == 0) {
            return new AttributeModifier(UUID.fromString(usedUUID), id, calculatePower(index), AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
        return new AttributeModifier(UUID.fromString(usedUUID), id, calculatePower(index), AttributeModifier.Operation.ADDITION);
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

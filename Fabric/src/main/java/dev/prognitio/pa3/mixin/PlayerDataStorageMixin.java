package dev.prognitio.pa3.mixin;

import dev.prognitio.pa3.AttributeType;
import dev.prognitio.pa3.FabricAbilityType;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.function.Supplier;

@Mixin(Player.class)
public abstract class PlayerDataStorageMixin implements IPlayerAttrStorage {

    private CompoundTag persistentData;

    @Override
    public CompoundTag getPersistentData() {
        if (persistentData == null) {
            persistentData = new CompoundTag();
            persistentData.putInt("availablepoints", 0);
            persistentData.putInt("level", 0);
            persistentData.putInt("experience", 0);
            persistentData.putInt("cooldown", 0);
            persistentData.putInt("maxcooldown", 0);
            persistentData.putInt("passivedodge", 0);
            persistentData.putInt("passiveparry", 0);
            persistentData.putInt("passivedoublestrike", 0);

            persistentData.putString("primaryabil", "dash");
            persistentData.putString("secondaryabil", "arrowsalvo");
            persistentData.putString("lastusedabil", "");

            //defaults for attributes and abilities
            Supplier<ArrayList<Double>> fitnessSupp = () -> {
                ArrayList<Double> list = new ArrayList<>();
                list.add(2.0);
                return list;
            };
            AttributeType fitness = new AttributeType("fitness", 10,
                    "5c93580c-84a1-4583-83a9-3c29ca3abb3e", fitnessSupp.get(), 1);

            Supplier<ArrayList<Double>> resilienceSupp = () -> {
                ArrayList<Double> list = new ArrayList<>();
                list.add(2.0);
                list.add(1.0);
                return list;
            };
            AttributeType resilience = new AttributeType("resilience", 10,
                    "e19e60ec-c913-433b-b38a-3801834be5cf", resilienceSupp.get(), 1);

            Supplier<ArrayList<Double>> combatSupp = () -> {
                ArrayList<Double> list = new ArrayList<>();
                list.add(0.5);
                list.add(5.0); //parry amount per level, start at 25, max at 75
                return list;
            };
            AttributeType combat = new AttributeType("combat", 10,
                    "b7b13584-e7d3-4dea-9d09-f6c36b9bef2c", combatSupp.get(), 1);

            Supplier<ArrayList<Double>> nimblenessSupp = () -> {
                ArrayList<Double> list = new ArrayList<>();
                list.add(0.1);
                list.add(3.0); //dodge chance scaling
                return list;
            };
            AttributeType nimbleness = new AttributeType("nimbleness", 10,
                    "db7daa72-f2e0-40b0-8120-5816bf0ba274", nimblenessSupp.get(), 1);

            Supplier<ArrayList<Double>> strategySupp = () -> {
                ArrayList<Double> list = new ArrayList<>();
                list.add(0.5);
                list.add(4.0);
                return list;
            };
            AttributeType strategy = new AttributeType("strategy", 10,
                    "5f14399f-df13-4007-bd76-7f5cbad40f9f", strategySupp.get(), 1);

            //abilities
            FabricAbilityType dash = new FabricAbilityType("dash", 5, 6, -1, 2, 1);
            FabricAbilityType arrowSalvo = new FabricAbilityType("arrowsalvo", 5, 5, +1, 3, 2);
            FabricAbilityType overshield = new FabricAbilityType("overshield", 5, 16, +2, 3, 2);
            FabricAbilityType incendiaryLance = new FabricAbilityType("incendiarylance", 5, 15, -1, 3, 1);
            FabricAbilityType chainLightning = new FabricAbilityType("chainlightning", 5, 15, -1, 3, 2);
            FabricAbilityType deflectiveShield = new FabricAbilityType("deflectiveshield", 5, 8, -1, 2, 1);

            persistentData.putString("fitness", fitness.toString());
            persistentData.putString("nimbleness", nimbleness.toString());
            persistentData.putString("resilience", resilience.toString());
            persistentData.putString("combat", combat.toString());
            persistentData.putString("strategy", strategy.toString());

            persistentData.putString("dash", dash.toString());
            persistentData.putString("arrowsalvo", arrowSalvo.toString());
            persistentData.putString("overshield", overshield.toString());
            persistentData.putString("incendiarylance", incendiaryLance.toString());
            persistentData.putString("chainlightning", chainLightning.toString());
            persistentData.putString("deflectiveshield", deflectiveShield.toString());
        }
        return persistentData;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void injectWriteMethod(CompoundTag compoundTag, CallbackInfo ci) {
        if(persistentData != null) {
            compoundTag.put("pa3.attr_abil_data", persistentData);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void injectReadMethod(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("pa3.attr_abil_data", 10)) {
            persistentData = compoundTag.getCompound("pa3.attr_abil_data");
        }
    }
}

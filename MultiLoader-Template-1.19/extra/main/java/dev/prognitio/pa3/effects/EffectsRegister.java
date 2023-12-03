package dev.prognitio.pa3.effects;

import dev.prognitio.pa3.pa3;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectsRegister {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, pa3.MODID);

    public static final RegistryObject<MobEffect> FALL_NEGATE_EFFECT = EFFECTS.register("fall_negate", () ->
        new NegateFallEffect(MobEffectCategory.BENEFICIAL, 0xfff1f1f1));

    public static final RegistryObject<MobEffect> ATTACK_NEGATE_EFFECT = EFFECTS.register("attack_negate", () ->
            new NegateAttackEffect(MobEffectCategory.BENEFICIAL, 0xffA10000));

    public static final RegistryObject<MobEffect> ARROW_SCATTER_EFFECT = EFFECTS.register("arrow_scatter", () ->
            new ArrowScatterEffect(MobEffectCategory.BENEFICIAL, 0xff888888));

    public static final RegistryObject<MobEffect> DEFENSIVE_SHIELD_EFFECT = EFFECTS.register("defensive_shield", () ->
            new DeflectiveShieldEffect(MobEffectCategory.BENEFICIAL, 0xff888888));

    public static final RegistryObject<MobEffect> CHAIN_LIGHTNING_INVULNERABLE = EFFECTS.register("chain_lightning_invulnerable", () ->
            new NegateAttackEffect(MobEffectCategory.BENEFICIAL, 0xffA10000));
}

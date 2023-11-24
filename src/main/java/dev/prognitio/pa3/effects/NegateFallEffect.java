package dev.prognitio.pa3.effects;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NegateFallEffect extends MobEffect {
    protected NegateFallEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Negate Fall");
    }
}

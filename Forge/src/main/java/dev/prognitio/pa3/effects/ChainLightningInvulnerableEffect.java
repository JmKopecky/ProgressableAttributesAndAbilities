package dev.prognitio.pa3.effects;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class ChainLightningInvulnerableEffect extends MobEffect {
    protected ChainLightningInvulnerableEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Chain Lightning Invulnerable");
    }
}

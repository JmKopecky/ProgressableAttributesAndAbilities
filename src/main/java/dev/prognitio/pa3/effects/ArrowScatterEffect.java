package dev.prognitio.pa3.effects;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public class ArrowScatterEffect extends MobEffect {
    protected ArrowScatterEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Random random = new Random();
        for (int i = 0; i < 1 + pAmplifier; i++) {
            System.out.println("spawning arrow");
            AbstractArrow arrow = new AbstractArrow(EntityType.ARROW, pLivingEntity, pLivingEntity.level) {
                @Override
                protected ItemStack getPickupItem() {
                    return ItemStack.EMPTY;
                }
            };
            float xRot = pLivingEntity.getXRot();
            xRot += random.nextFloat(0, 20);
            xRot -= random.nextFloat(0, 20);
            float yRot = pLivingEntity.getYRot();
            yRot += random.nextFloat(0, 20);
            yRot -= random.nextFloat(0, 20);
            float vel = random.nextFloat(3, 6);
            arrow.shootFromRotation(pLivingEntity, xRot, yRot, 0.0f, vel, 0.5f);
            arrow.setBaseDamage(arrow.getBaseDamage() + 4);
            arrow.setCritArrow(true);
            pLivingEntity.level.addFreshEntity(arrow);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Arrow Scatter");
    }
}

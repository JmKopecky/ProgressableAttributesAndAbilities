package dev.prognitio.pa3.effects;

import dev.prognitio.pa3.ParticleRegister;
import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeflectiveShieldEffect extends MobEffect {
    protected DeflectiveShieldEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        int level = pAmplifier + 1;

        int radius = 6;
        double xPos = pLivingEntity.getX();
        double yPos = pLivingEntity.getY();
        double zPos = pLivingEntity.getZ();

        AABB area = new AABB(
                xPos + radius, yPos + radius, zPos + radius,
                xPos - radius, yPos - radius, zPos - radius);

        ArrayList<Projectile> potentialTargets = (ArrayList<Projectile>) pLivingEntity.level.getEntitiesOfClass(Projectile.class, area);
        ArrayList<Projectile> actualTargets = new ArrayList<>();
        for (Projectile target : potentialTargets) {
            if (pLivingEntity.distanceTo(target) < radius) {
                if (!(target.getOwner() != null && target.getOwner().is(pLivingEntity))) {
                    actualTargets.add(target);
                }
            }
        }

        for (Projectile target: actualTargets) {
            if (pLivingEntity instanceof Player && target.getOwner() != null) {
                pLivingEntity.getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
                    if (cap.deflectiveShield.isEliteVersion) {
                        target.lookAt(EntityAnchorArgument.Anchor.EYES, target.getOwner().getEyePosition());
                        target.shootFromRotation(target.getOwner(), target.getXRot(), target.getYRot(), 0, 2.5f, 0f);
                        target.getLevel().addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY(), target.getZ(), 0, 0, 0);
                    } else {
                        notElite(pLivingEntity, target, level);
                    }
                });
            } else {
                notElite(pLivingEntity, target, level);
            }
        }

        if (pLivingEntity.getEffect(EffectsRegister.DEFENSIVE_SHIELD_EFFECT.get()).getDuration() % 4 == 0) {
            if (pLivingEntity.level.isClientSide) {
                renderParticles(pLivingEntity, radius);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Deflective Shield");
    }

    private void notElite(LivingEntity entity, Projectile target, int level) {
        AbstractArrow getPosWithExtraEntity = new AbstractArrow(EntityType.ARROW, entity, entity.level) {
            @Override
            protected ItemStack getPickupItem() {
                return null;
            }
        };
        getPosWithExtraEntity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
        Vec2 newRot = new Vec2(getPosWithExtraEntity.getXRot(), getPosWithExtraEntity.getYRot());
        getPosWithExtraEntity.discard();

        double yAngleModifier = Math.random() * 90.0;
        if (Math.random() * 2 > 1) {
            yAngleModifier *= -1;
        }
        newRot = new Vec2(newRot.x, (float) (newRot.y + yAngleModifier));

        if (target.getOwner() != null) {
            target.shootFromRotation(target.getOwner(), newRot.x, newRot.y, 0, 1.5f, 1f);
            target.getLevel().addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY(), target.getZ(), 0, 0, 0);
        } else {
            target.shootFromRotation(entity, newRot.x, newRot.y, 0, 1.5f, 1);
            target.getLevel().addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY(), target.getZ(), 0, 0, 0);
        }
    }

    private void renderParticles(LivingEntity entity, double radius) {
        Level level = entity.getLevel();
        Vec3 pos = entity.position();
        pos.add(0, 4, 0);
        ArrayList<Vec3> points = resolvePoints(pos, radius);
        for (Vec3 point : points) {
            level.addParticle(ParticleRegister.DEFENSIVE_SHIELD_PARTICLE.get(), point.x, point.y, point.z, 0, 0, 0);
        }
    }

    private ArrayList<Vec3> resolvePoints(Vec3 startPos, double radius) {
        ArrayList<Vec3> output = new ArrayList<>();

        for (int horAngle = 0; horAngle < 360; horAngle++) {
            if (horAngle % 5 == 0) {
                for (int verAngle = 0; verAngle < 360; verAngle++) {
                    if (verAngle % 5 == 0) {
                        Vec3 point = new Vec3(startPos.x, startPos.y, startPos.z);
                        double yToAdd = radius / (1.0 / (Math.sin(Math.toRadians(verAngle))));
                        double xToAdd = radius / (1.0 / (Math.sin(Math.toRadians(horAngle))));
                        double zToAdd = radius / (1.0 / (Math.cos(Math.toRadians(horAngle))));
                        int isXNeg = (xToAdd < 0) ? -1 : 1;
                        int isZNeg = (zToAdd < 0) ? -1 : 1;
                        int isYNeg = (yToAdd < 0) ? -1 : 1;

                        xToAdd = Math.sqrt(Math.pow(xToAdd, 2) - Math.pow(yToAdd, 2));
                        zToAdd = Math.sqrt(Math.pow(zToAdd, 2) - Math.pow(yToAdd, 2));

                        xToAdd *= isXNeg;
                        zToAdd *= isZNeg;
                        output.add(point.add(xToAdd, yToAdd, zToAdd));
                    }
                }
            }
        }

        return output;
    }
}

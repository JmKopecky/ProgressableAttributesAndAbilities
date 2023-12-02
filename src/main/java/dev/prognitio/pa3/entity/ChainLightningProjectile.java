package dev.prognitio.pa3.entity;

import dev.prognitio.pa3.capabililty.AttributesProvider;
import dev.prognitio.pa3.effects.EffectsRegister;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;

public class ChainLightningProjectile extends AbstractArrow {

    int chainCount;
    LivingEntity target;

    public ChainLightningProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ChainLightningProjectile(EntityType<? extends AbstractArrow> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public ChainLightningProjectile(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel, int chainCount, LivingEntity target) {
        super(pEntityType, pShooter, pLevel);
        this.chainCount = chainCount;
        this.target = target;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 200) {
            discard();
            return;
        }
    }

    @Override
    public void push(double x, double y, double z) {
        if (getOwner() != null && getOwner() instanceof Player) {
            getOwner().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
                if (!cap.chainLightning.isEliteVersion) {
                    super.push(x, y, z);
                }
            });
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        if (getOwner() instanceof Player) {
            getOwner().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
                boolean isElite = cap.chainLightning.isEliteVersion;
                int lightningLevel = cap.chainLightning.level;
                int maxChainCount = lightningLevel * 2;
                int damage = 6 + lightningLevel * 4;
                int range = 4 + lightningLevel * 2;
                if (chainCount <= maxChainCount) {
                    if (result.getEntity() instanceof LivingEntity) {
                        LivingEntity entity = (LivingEntity) result.getEntity();
                        entity.hurt(DamageSource.LIGHTNING_BOLT, damage);

                        if (!level.isClientSide && isElite) {
                            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                            bolt.setPos(entity.position());
                            bolt.setDamage(bolt.getDamage() * 2);
                            bolt.setVisualOnly(false);
                            entity.invulnerableTime = 0;
                            entity.setInvulnerable(false);
                            getLevel().addFreshEntity(bolt);
                        }

                        entity.addEffect(new MobEffectInstance(EffectsRegister.CHAIN_LIGHTNING_INVULNERABLE.get(), 100, 0));

                        //extra stuff to target / chain to next entity

                        double entityX = entity.getX();
                        double entityY = entity.getY();
                        double entityZ = entity.getZ();

                        AABB area = new AABB(
                                entityX + range, entityY + range, entityZ + range, entityX - range, entityY - range, entityZ - range);
                        ArrayList<LivingEntity> entities = (ArrayList<LivingEntity>) entity.level.getEntitiesOfClass(LivingEntity.class, area);
                        ArrayList<LivingEntity> targetList = new ArrayList<>();

                        for (LivingEntity filteredTarget : entities) {
                            if (!filteredTarget.hasEffect(EffectsRegister.CHAIN_LIGHTNING_INVULNERABLE.get())) {
                                targetList.add(filteredTarget);
                            }
                        }

                        if (targetList.size() != 0) {
                            LivingEntity newTarget = entity.level.getNearestEntity(targetList, TargetingConditions.forCombat(), null, entityX, entityY, entityZ);

                            AbstractArrow getPos = new AbstractArrow(EntityType.ARROW, entity, entity.level) {
                                @Override
                                protected ItemStack getPickupItem() {
                                    return null;
                                }
                            };

                            getPos.lookAt(EntityAnchorArgument.Anchor.EYES, newTarget.getEyePosition());
                            Vec2 lookRot = new Vec2(getPos.getXRot(), getPos.getYRot());
                            getPos.discard();

                            ChainLightningProjectile projectile = new ChainLightningProjectile(EntityRegister.CHAIN_LIGHTNING.get(), (Player) getOwner(), entity.level, chainCount + 1, newTarget);
                            projectile.setOwner(getOwner());
                            projectile.setPos(entity.getEyePosition());
                            projectile.shootFromRotation(entity, lookRot.x, lookRot.y, 0, 0.75f, 1f);
                            entity.level.addFreshEntity(projectile);
                        }
                    }
                }
            });
        }
        discard();
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        if (!level.isClientSide) {
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, getLevel());
            bolt.setPos(result.getLocation());
            bolt.setDamage(bolt.getDamage() * 2);
            bolt.setVisualOnly(false);
            getLevel().addFreshEntity(bolt);
        }
        discard();
    }
}

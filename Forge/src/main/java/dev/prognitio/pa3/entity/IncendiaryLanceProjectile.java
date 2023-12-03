package dev.prognitio.pa3.entity;

import dev.prognitio.pa3.capabililty.AttributesProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class IncendiaryLanceProjectile extends AbstractArrow {
    public IncendiaryLanceProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IncendiaryLanceProjectile(EntityType<? extends AbstractArrow> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public IncendiaryLanceProjectile(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tickDespawn() {
        if (this.inGround) {
            this.discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity owner = this.getOwner();
        if (owner instanceof Player) {
            owner.getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
                result.getEntity().hurt(DamageSource.LAVA, (float) this.getBaseDamage());
                int level = cap.incendiaryLance.level;
                result.getEntity().setSecondsOnFire(2 * level);
                if (cap.incendiaryLance.isEliteVersion) {
                    this.getLevel().explode(this, this.getX(), this.getY(), this.getZ(), 6.0f, true, Explosion.BlockInteraction.BREAK);
                } else {
                    this.getLevel().explode(this, this.getX(), this.getY(), this.getZ(), 2.0f, true, Explosion.BlockInteraction.BREAK);
                }
            });
        }
        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        if (getOwner() instanceof Player) {
            getOwner().getCapability(AttributesProvider.ATTRIBUTES).ifPresent((cap) -> {
                if (cap.incendiaryLance.isEliteVersion) {
                    this.getLevel().explode(this, this.getX(), this.getY(), this.getZ(), 6.0f, true, Explosion.BlockInteraction.BREAK);
                } else {
                    this.getLevel().explode(this, this.getX(), this.getY(), this.getZ(), 2.0f, true, Explosion.BlockInteraction.BREAK);
                }
            });
        }
        this.discard();
    }
}

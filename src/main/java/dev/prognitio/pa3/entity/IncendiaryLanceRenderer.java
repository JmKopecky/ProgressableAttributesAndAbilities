package dev.prognitio.pa3.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.pa3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class IncendiaryLanceRenderer extends ArrowRenderer<IncendiaryLanceProjectile> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(pa3.MODID, "textures/entity/incendiarylance.png");

    public IncendiaryLanceRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(IncendiaryLanceProjectile pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(IncendiaryLanceProjectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.pushPose();
        pMatrixStack.scale(20f, 20f, 20f);
        if (pEntity.getLevel().isClientSide()) {
            pEntity.getLevel().addParticle(ParticleTypes.SMALL_FLAME, pEntity.getX(), pEntity.getY(), pEntity.getZ(), 0, 0, 0);
            Random random = new Random();
            for (int i = 0; i < 30; i++) {
                Vec3 movementVector = pEntity.getDeltaMovement();
                float xDir = (float) (random.nextFloat(-1, 1) * 0.1);
                float yDir = (float) (random.nextFloat(-1, 1) * 0.1);
                float zDir = (float) (random.nextFloat(-1, 1) * 0.1);
                int particleType = random.nextInt() % 2;
                if (particleType == 0) {
                    pEntity.getLevel().addParticle(ParticleTypes.SMALL_FLAME, pEntity.getX(), pEntity.getY(), pEntity.getZ(), xDir, yDir, zDir);
                } else {
                    pEntity.getLevel().addParticle(ParticleTypes.ASH, pEntity.getX(), pEntity.getY(), pEntity.getZ(), xDir, yDir, zDir);
                }
            }
        }
        pMatrixStack.popPose();
    }
}

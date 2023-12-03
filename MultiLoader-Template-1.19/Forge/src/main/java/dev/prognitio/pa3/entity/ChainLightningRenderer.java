package dev.prognitio.pa3.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.Constants;
import dev.prognitio.pa3.Pa3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ChainLightningRenderer extends ArrowRenderer<ChainLightningProjectile> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/chainlightning.png");

    public ChainLightningRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(ChainLightningProjectile pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(ChainLightningProjectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        if (pEntity.getLevel().isClientSide()) {
            pEntity.getLevel().addParticle(ParticleTypes.SCRAPE, pEntity.getX(), pEntity.getY(), pEntity.getZ(), 0, 0, 0);
            Random random = new Random();
            for (int i = 0; i < 30; i++) {
                Vec3 movementVector = pEntity.getDeltaMovement();

                float xDir = random.nextFloat(-1, 1) * 5;
                float yDir = random.nextFloat(-1, 1) * 5;
                float zDir = random.nextFloat(-1, 1) * 5;

                pEntity.getLevel().addParticle(ParticleTypes.WAX_OFF, pEntity.getX(), pEntity.getY(), pEntity.getZ(), xDir, yDir, zDir);
            }
        }
    }
}

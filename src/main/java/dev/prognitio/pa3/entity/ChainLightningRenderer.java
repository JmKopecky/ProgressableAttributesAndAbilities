package dev.prognitio.pa3.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.pa3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

public class ChainLightningRenderer extends ArrowRenderer<ChainLightningProjectile> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(pa3.MODID, "textures/entity/chainlightning.png");

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
            RenderSystem.setShaderColor(0f, 1.0f, 1.0f, 1.0f);
            pEntity.getLevel().addParticle(ParticleTypes.ELECTRIC_SPARK, pEntity.getX(), pEntity.getY(), pEntity.getZ(), 0, 0, 0);
        }
    }
}

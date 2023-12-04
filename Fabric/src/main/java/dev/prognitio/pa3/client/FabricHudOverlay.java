package dev.prognitio.pa3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.Constants;
import dev.prognitio.pa3.playerdata.PlayerDataGetters;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class FabricHudOverlay implements HudRenderCallback {

    private static final ResourceLocation COOLDOWN = new ResourceLocation(Constants.MOD_ID, "textures/gui/cooldownbars.png");
    //private static final ResourceLocation PASSIVE_TEMPLATE = new ResourceLocation(pa3.MODID, "textures/gui/passiveicontemplate.png");
    private static final ResourceLocation PASSIVE_DODGE = new ResourceLocation(Constants.MOD_ID, "textures/gui/passivedodgeicontemplate.png");
    private static final ResourceLocation PASSIVE_PARRY = new ResourceLocation(Constants.MOD_ID, "textures/gui/passiveparryicontemplate.png");
    private static final ResourceLocation PASSIVE_DOUBLE_STRIKE = new ResourceLocation(Constants.MOD_ID, "textures/gui/passivedoublestrikeicontemplate.png");

    @Override
    public void onHudRender(PoseStack matrixStack, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client != null) {
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, COOLDOWN);

            IPlayerAttrStorage data = ((IPlayerAttrStorage) client.player);
            if (data != null) {
                int xPos = width/2 - (182/2);
                int yPos = 10;
                int cooldown = PlayerDataGetters.getAbilityCooldown(data);
                int maxCooldown = PlayerDataGetters.getCurrentMaxCooldown(data);
                if (maxCooldown != 0) {
                    double percentage = cooldown * 1.0 / maxCooldown;
                    int fillValue = (int) (182 * percentage);
                    if (fillValue != 0) {
                        GuiComponent.blit(matrixStack, xPos, yPos, 0, 0, 182, 5, 182, 10);
                        GuiComponent.blit(matrixStack, xPos, yPos, 0, 5, fillValue, 5, 182, 10);
                    }
                }

                int dodgeTimer = PlayerDataGetters.getPassiveDodge(data);
                int parryTimer = PlayerDataGetters.getPassiveParry(data);
                int doubleStrikeTimer = PlayerDataGetters.getPassiveDoubleStrike(data);

                matrixStack.pushPose();
                matrixStack.scale(0.8f, 0.8f, 0.8f);

                xPos = (int) (width / 0.8 - 32 / 0.8 - 10 / 0.8);

                if (dodgeTimer != 0) {
                    RenderSystem.setShaderTexture(0, PASSIVE_DODGE);
                    yPos = height / 2 + 140;
                    GuiComponent.blit(matrixStack, xPos, yPos, 0, 0, 32, 32, 32, 32);
                }
                if (parryTimer != 0) {
                    RenderSystem.setShaderTexture(0, PASSIVE_PARRY);
                    yPos = height / 2 + 100;
                    GuiComponent.blit(matrixStack, xPos, yPos, 0, 0, 32, 32, 32, 32);
                }
                if (doubleStrikeTimer != 0) {
                    RenderSystem.setShaderTexture(0, PASSIVE_DOUBLE_STRIKE);
                    yPos = height / 2 + 60;
                    GuiComponent.blit(matrixStack, xPos, yPos, 0, 0, 32, 32, 32, 32);
                }

                matrixStack.popPose();
            }
        }
    }
}

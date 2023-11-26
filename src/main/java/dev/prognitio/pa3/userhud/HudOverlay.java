package dev.prognitio.pa3.userhud;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.prognitio.pa3.ClientDataStorage;
import dev.prognitio.pa3.pa3;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HudOverlay {

    private static final ResourceLocation COOLDOWN = new ResourceLocation(pa3.MODID, "textures/gui/cooldownbars.png");
    private static final ResourceLocation PASSIVE_TEMPLATE = new ResourceLocation(pa3.MODID, "textures/gui/passiveicontemplate.png");
    private static final ResourceLocation PASSIVE_DODGE = new ResourceLocation(pa3.MODID, "textures/gui/passivedodgeicontemplate.png");
    private static final ResourceLocation PASSIVE_PARRY = new ResourceLocation(pa3.MODID, "textures/gui/passiveparryicontemplate.png");
    private static final ResourceLocation PASSIVE_DOUBLE_STRIKE = new ResourceLocation(pa3.MODID, "textures/gui/passivedoublestrikeicontemplate.png");


    public static final IGuiOverlay HUD_PA3 = ((gui, poseStack, partialTick, width, height) -> {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, COOLDOWN);

        int xPos = width/2 - (182/2);
        int yPos = 10;
        double percentage = ClientDataStorage.getCurrentCooldown() * 1.0 / ClientDataStorage.getMaxCooldown();
        int fillValue = (int) (182 * percentage);
        if (fillValue != 0) {
            GuiComponent.blit(poseStack, xPos, yPos, 0, 0, 182, 5, 182, 10);
            GuiComponent.blit(poseStack, xPos, yPos, 0, 5, fillValue, 5, 182, 10);
        }



        int dodgeTimer = ClientDataStorage.getPassiveProcTimer("dodge");
        int parryTimer = ClientDataStorage.getPassiveProcTimer("parry");
        int doubleStrikeTimer = ClientDataStorage.getPassiveProcTimer("doublestrike");

        poseStack.pushPose();
        poseStack.scale(0.8F, 0.8F, 0.8F);

        xPos = (int) (width / 0.8 - 32 / 0.8 - 10 / 0.8);

        if (dodgeTimer != 0) {
            RenderSystem.setShaderTexture(0, PASSIVE_DODGE);
            yPos = height / 2 + 140;
            GuiComponent.blit(poseStack, xPos, yPos, 0, 0, 32, 32, 32, 32);
        }
        if (parryTimer != 0) {
            RenderSystem.setShaderTexture(0, PASSIVE_PARRY);
            yPos = height / 2 + 100;
            GuiComponent.blit(poseStack, xPos, yPos, 0, 0, 32, 32, 32, 32);
        }
        if (doubleStrikeTimer != 0) {
            RenderSystem.setShaderTexture(0, PASSIVE_DOUBLE_STRIKE);
            yPos = height / 2 + 60;
            GuiComponent.blit(poseStack, xPos, yPos, 0, 0, 32, 32, 32, 32);
        }

        poseStack.popPose();
    });
}

package dev.prognitio.pa3.userinterface;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.ModNetworking;
import dev.prognitio.pa3.pa3;
import dev.prognitio.pa3.userinterface.packets.LevelUpAbilityCS;
import dev.prognitio.pa3.userinterface.packets.SetSelectedAbilityCS;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Optional;

public class AbilityDisplayScreen extends Screen {

    Window window;
    int midY;
    int midX;
    ResourceLocation bg = new ResourceLocation(pa3.MODID, "textures/gui/attr_abil_bg.png");

    public AbilityDisplayScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        window = Minecraft.getInstance().getWindow();
        midY = window.getGuiScaledHeight() / 2;
        midX = window.getGuiScaledWidth() / 2;
        this.addRenderableWidget(new Button(midX - (60/2), midY + (256/2) - 10,
                60, 20, Component.literal("Attributes"),
                (button) -> {
                    Minecraft.getInstance().setScreen(new AttributeDisplayScreen(Component.literal("Attribute Interface")));
                }));


        //button to level up abilities
        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 60 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> {
                    //level up dash
                    ModNetworking.sendToServer(new LevelUpAbilityCS("dash"));
                }, (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            int level = Integer.parseInt(ClientDataStorage.getAbilityProperty("dash", "level"));
            if (level == 0) {
                tooltip.add(Component.literal("Unlock").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            } else {
                tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            }
            int maxLevel = Integer.parseInt(ClientDataStorage.getAbilityProperty("dash", "max"));
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = Integer.parseInt(ClientDataStorage.getAbilityProperty("dash", "upgrade"));
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 40 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> {
                    //level up arrow salvo
                    ModNetworking.sendToServer(new LevelUpAbilityCS("arrowsalvo"));
                }, (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            int level = Integer.parseInt(ClientDataStorage.getAbilityProperty("arrowsalvo", "level"));
            if (level == 0) {
                tooltip.add(Component.literal("Unlock").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            } else {
                tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            }
            int maxLevel = Integer.parseInt(ClientDataStorage.getAbilityProperty("arrowsalvo", "max"));
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = Integer.parseInt(ClientDataStorage.getAbilityProperty("arrowsalvo", "upgrade"));
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 20 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> {
                    //level up overshield
                    ModNetworking.sendToServer(new LevelUpAbilityCS("overshield"));
                }, (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            int level = Integer.parseInt(ClientDataStorage.getAbilityProperty("overshield", "level"));
            if (level == 0) {
                tooltip.add(Component.literal("Unlock").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            } else {
                tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            }
            int maxLevel = Integer.parseInt(ClientDataStorage.getAbilityProperty("overshield", "max"));
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = Integer.parseInt(ClientDataStorage.getAbilityProperty("overshield", "upgrade"));
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));


    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        RenderSystem.setShaderTexture(0, bg);
        blit(stack, (window.getGuiScaledWidth() - 256) / 2, (window.getGuiScaledHeight() - 256) / 2, 0, 0, 0, 256, 256, 256, 256);

        String currentLevel = String.valueOf(ClientDataStorage.getLevel());
        String currentXp = String.valueOf(ClientDataStorage.getXp());
        String requiredXp = String.valueOf(ClientDataStorage.getRequiredXp());
        String availablePoints = String.valueOf(ClientDataStorage.getAvailablePoints());
        String levelDisplay = "Level: " + currentLevel + " | XP: " + currentXp + "/" + requiredXp + " | Available Points: " + availablePoints;
        this.font.draw(stack, levelDisplay, (float) (midX - font.width(levelDisplay) / 2), (float) ((midY - 100)), 0xff4d4d4d);

        renderAbilityTitle("Dash", midX - 80, midY - 60, mouseX, mouseY, stack);
        renderAbilityTitle("Arrow Salvo", midX - 80, midY - 40, mouseX, mouseY, stack);
        renderAbilityTitle("Overshield", midX - 80,  midY - 20, mouseX, mouseY, stack);

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    protected void renderAbilityTitle(String title, int xPos, int yPos, int mouseX, int mouseY, PoseStack stack) {
        boolean isPrimary = ClientDataStorage.getPrimaryAbility().equals(title.toLowerCase().replaceAll(" ", ""));
        boolean isSecondary = ClientDataStorage.getSecondaryAbility().equals(title.toLowerCase().replaceAll(" ", ""));

        float xStart = (float) (xPos - (font.width(title)/2.0));
        if (isPrimary) {
            this.font.draw(stack, title, xStart, yPos, 11141290);
        } else if (isSecondary) {
            this.font.draw(stack, title, xStart, yPos, 11141120);
        } else {
            this.font.draw(stack, title, xStart, yPos, 0xff4d4d4d);
        }

        boolean isOverlappingText = isInRange(mouseX, mouseY, (int) xStart, font.width(title), yPos, font.lineHeight);
        if (isOverlappingText) {
            ArrayList<Component> toRender = new ArrayList<>();
            toRender.add(Component.literal(title).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE));
            String level = ClientDataStorage.getAbilityProperty(title.replaceAll(" ", "").toLowerCase(), "level");;
            String maxLevel = ClientDataStorage.getAbilityProperty(title.replaceAll(" ", "").toLowerCase(), "max");
            toRender.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.AQUA));
            String desc = ClientDataStorage.getAbilityProperty(title.replaceAll(" ", "").toLowerCase(), "desc");
            toRender.add(Component.literal(desc).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.WHITE));
            toRender.add(Component.literal("Left click to set as primary.").withStyle(ChatFormatting.DARK_PURPLE));
            toRender.add(Component.literal("Right click to set as secondary").withStyle(ChatFormatting.DARK_RED));
            this.renderTooltip(stack, toRender, Optional.empty(), mouseX, mouseY, font);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        String ability = "";

        if (isInRange(pMouseX, pMouseY, (midX - 80) - (font.width("Dash")/2), font.width("Dash"), midY - 60, font.lineHeight)) {
            ability = "dash";
        }
        if (isInRange(pMouseX, pMouseY, (midX - 80) - (font.width("Arrow Salvo")/2), font.width("Arrow Salvo"), midY - 40, font.lineHeight)) {
            ability = "arrowsalvo";
        }
        if (isInRange(pMouseX, pMouseY, (midX - 80) - (font.width("Overshield")/2), font.width("Overshield"), midY - 20, font.lineHeight)) {
            ability = "overshield";
        }

        if (!ability.equals("")) {
            if (pButton == 0) { //left click
                ModNetworking.sendToServer(new SetSelectedAbilityCS(ability + ":p"));
            } else { //right click
                ModNetworking.sendToServer(new SetSelectedAbilityCS(ability + ":s"));
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean isInRange(double mouseX, double mouseY, int xStart, int width, int yStart, int height) {
        boolean xVal = mouseX >= xStart && mouseX <= xStart + width;
        boolean yVal = mouseY >= yStart && mouseY <= yStart + height;
        return xVal && yVal;
    }
}



package dev.prognitio.pa3.userinterface;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.ClientDataStorage;
import dev.prognitio.pa3.ModNetworking;
import dev.prognitio.pa3.capabililty.AbilityType;
import dev.prognitio.pa3.pa3;
import dev.prognitio.pa3.userinterface.packets.LevelUpAbilityCS;
import dev.prognitio.pa3.userinterface.packets.SetSelectedAbilityCS;
import dev.prognitio.pa3.userinterface.packets.UnlockEliteAbilityCS;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public class AbilityDisplayScreen extends Screen {

    Window window;
    int midY;
    int midX;
    ResourceLocation bg = new ResourceLocation(pa3.MODID, "textures/gui/attr_abil_bg.png");
    ResourceLocation bar = new ResourceLocation(pa3.MODID, "textures/gui/cooldownbars.png");

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
                (button) -> Minecraft.getInstance().setScreen(new AttributeDisplayScreen(Component.literal("Attribute Interface")))));


        //button to level up abilities
        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 60 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> upgradeAbilityButtonOnClick("dash"), (button, stack, mx, my) -> renderUpgradeButtonTooltip("dash", stack, mx, my)));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 40 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> upgradeAbilityButtonOnClick("arrowsalvo"), (button, stack, mx, my) -> renderUpgradeButtonTooltip("arrowsalvo", stack, mx, my)));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 20 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> upgradeAbilityButtonOnClick("overshield"), (button, stack, mx, my) -> renderUpgradeButtonTooltip("overshield", stack, mx, my)));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> upgradeAbilityButtonOnClick("incendiarylance"), (button, stack, mx, my) -> renderUpgradeButtonTooltip("incendiarylance", stack, mx, my)));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY + 20 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> upgradeAbilityButtonOnClick("chainlightning"), (button, stack, mx, my) -> renderUpgradeButtonTooltip("chainlightning", stack, mx, my)));

        this.addRenderableWidget(new Button(midX - 20 - 10, midY + 40 - 10 + font.lineHeight/2,
                20, 20, Component.literal("+"),
                (button) -> upgradeAbilityButtonOnClick("deflectiveshield"), (button, stack, mx, my) -> renderUpgradeButtonTooltip("deflectiveshield", stack, mx, my)));

    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        RenderSystem.setShaderTexture(0, bg);
        blit(stack, (window.getGuiScaledWidth() - 256) / 2, (window.getGuiScaledHeight() - 256) / 2, 0, 0, 0, 256, 256, 256, 256);

        RenderSystem.setShaderTexture(0, bar);
        int currentXp = ClientDataStorage.getXp();
        int requiredXp = ClientDataStorage.getRequiredXp();
        double percent = currentXp * 1.0 / requiredXp;
        int fillValue = (int) (percent * 182);
        blit(stack, midX - (182 / 2), midY - 100, 0, 0, 182, 5, 182, 10);
        blit(stack, midX - (182/2), midY - 100, 0, 5, fillValue, 5, 182, 10);

        String currentLevel = String.valueOf(ClientDataStorage.getLevel());
        String availablePoints = String.valueOf(ClientDataStorage.getAvailablePoints());
        String levelDisplay = "Level: " + currentLevel + " | Available Points: " + availablePoints;
        this.font.draw(stack, levelDisplay, (float) (midX - font.width(levelDisplay) / 2), (float) ((midY - 115)), 0xff4d4d4d);

        renderAbilityTitle("Dash", midX - 80, midY - 60, mouseX, mouseY, stack);
        renderAbilityTitle("Arrow Salvo", midX - 80, midY - 40, mouseX, mouseY, stack);
        renderAbilityTitle("Overshield", midX - 80,  midY - 20, mouseX, mouseY, stack);
        renderAbilityTitle("Incendiary Lance", midX - 80, midY, mouseX, mouseY, stack);
        renderAbilityTitle("Chain Lightning", midX - 80,  midY + 20, mouseX, mouseY, stack);
        renderAbilityTitle("Deflective Shield", midX - 80,  midY + 40, mouseX, mouseY, stack);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    protected void renderAbilityTitle(String title, int xPos, int yPos, int mouseX, int mouseY, PoseStack stack) {
        boolean isPrimary = ClientDataStorage.getPrimaryAbility().equals(title.toLowerCase().replaceAll(" ", ""));
        boolean isSecondary = ClientDataStorage.getSecondaryAbility().equals(title.toLowerCase().replaceAll(" ", ""));
        boolean isElite = Boolean.parseBoolean(ClientDataStorage.getAbilityProperty(title, "elite"));

        float xStart = (float) (xPos - (font.width(title)/2.0));

        if (isPrimary) {
            if (isElite) {
                this.font.draw(stack, Component.literal(title).withStyle(ChatFormatting.BOLD), xStart, yPos, 11141290);
            } else {
                this.font.draw(stack, title, xStart, yPos, 11141290);
            }
        } else if (isSecondary) {
            if (isElite) {
                this.font.draw(stack, Component.literal(title).withStyle(ChatFormatting.BOLD), xStart, yPos, 11141120);
            } else {
                this.font.draw(stack, title, xStart, yPos, 11141120);
            }
        } else {
            if (isElite) {
                this.font.draw(stack, Component.literal(title).withStyle(ChatFormatting.BOLD), xStart, yPos, 0xff4d4d4d);
            } else {
                this.font.draw(stack, title, xStart, yPos, 0xff4d4d4d);
            }
        }

        boolean isOverlappingText = isInRange(mouseX, mouseY, (int) xStart, font.width(title), yPos, font.lineHeight);
        if (isOverlappingText) {
            ArrayList<Component> toRender = new ArrayList<>();
            toRender.add(Component.literal(title).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE));
            String level = ClientDataStorage.getAbilityProperty(title.replaceAll(" ", "").toLowerCase(), "level");
            String maxLevel = ClientDataStorage.getAbilityProperty(title.replaceAll(" ", "").toLowerCase(), "max");
            toRender.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.AQUA));
            String desc = ClientDataStorage.getAbilityProperty(title.replaceAll(" ", "").toLowerCase(), "desc");
            toRender.add(Component.literal(desc).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.WHITE));
            toRender.add(Component.literal("Left click to set as primary.").withStyle(ChatFormatting.DARK_PURPLE));
            toRender.add(Component.literal("Right click to set as secondary").withStyle(ChatFormatting.DARK_RED));
            this.renderTooltip(stack, toRender, Optional.empty(), mouseX, mouseY, font);
        }
    }

    public void renderUpgradeButtonTooltip(String type, PoseStack stack, int mx, int my) {
        ArrayList<Component> tooltip = new ArrayList<>();
        int level = Integer.parseInt(ClientDataStorage.getAbilityProperty(type, "level"));
        int maxLevel = Integer.parseInt(ClientDataStorage.getAbilityProperty(type, "max"));
        boolean isElite = Boolean.parseBoolean(ClientDataStorage.getAbilityProperty(type, "elite"));
        if (level == 0) {
            tooltip.add(Component.literal("Unlock").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
        } else if (level == maxLevel && !isElite) {
            tooltip.add(Component.literal("Unlock Elite Ability").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
        } else if (level == maxLevel) {
            tooltip.add(Component.literal("Elite Ability").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
        } else {
            tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
        }

        tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));

        if (level != maxLevel) {
            int upgradeCost = Integer.parseInt(ClientDataStorage.getAbilityProperty(type, "upgrade"));
            tooltip.add(Component.literal("Required Points: " + upgradeCost));
        }

        if (!isElite && level == maxLevel) {
            tooltip.add(Component.literal("Required Points: " + AbilityType.ELITE_ABILITY_COST));
        }
        Optional<TooltipComponent> optional = Optional.empty();
        renderTooltip(stack, tooltip, optional, mx, my);
    }

    public void upgradeAbilityButtonOnClick(String type) {
        int level = Integer.parseInt(ClientDataStorage.getAbilityProperty(type, "level"));
        int maxLevel = Integer.parseInt(ClientDataStorage.getAbilityProperty(type, "max"));
        if (level == maxLevel) {
            boolean isElite = Boolean.parseBoolean(ClientDataStorage.getAbilityProperty(type, "elite"));
            if (!isElite) {
                ModNetworking.sendToServer(new UnlockEliteAbilityCS(type));
            }
            return;
        }
        if (level < maxLevel) {
            ModNetworking.sendToServer(new LevelUpAbilityCS(type));
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
        if (isInRange(pMouseX, pMouseY, (midX - 80) - (font.width("Incendiary Lance")/2), font.width("IncendiaryLance"), midY, font.lineHeight)) {
            ability = "incendiarylance";
        }
        if (isInRange(pMouseX, pMouseY, (midX - 80) - (font.width("Chain Lightning")/2), font.width("Chain Lightning"), midY + 20, font.lineHeight)) {
            ability = "chainlightning";
        }
        if (isInRange(pMouseX, pMouseY, (midX - 80) - (font.width("Deflective Shield")/2), font.width("Deflective Shield"), midY + 40, font.lineHeight)) {
            ability = "deflectiveshield";
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



package dev.prognitio.pa3.userinterface;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.prognitio.pa3.ClientDataStorage;
import dev.prognitio.pa3.ModNetworking;
import dev.prognitio.pa3.pa3;
import dev.prognitio.pa3.userinterface.packets.LevelUpAttributeCS;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public class AttributeDisplayScreen extends Screen {

    public AttributeDisplayScreen(Component pTitle) {
        super(pTitle);
    }

    Window window;
    int midY = 0;
    int midX = 0;
    ResourceLocation bg = new ResourceLocation(pa3.MODID, "textures/gui/attr_abil_bg.png");
    ResourceLocation bar = new ResourceLocation(pa3.MODID, "textures/gui/cooldownbars.png");

    @Override
    protected void init() {
        window = Minecraft.getInstance().getWindow();
        midY = window.getGuiScaledHeight() / 2;
        midX = window.getGuiScaledWidth() / 2;
        this.addRenderableWidget(new Button(midX - (40/2), midY + (256/2) - 10,
                40, 20, Component.literal("Abilities"),
                (button) -> Minecraft.getInstance().setScreen(new AbilityDisplayScreen(Component.literal("Ability Interface")))));

        //fitness level up
        this.addRenderableWidget(new Button(midX + 95, midY - 45 - 5, 20, 20, Component.literal("+"),
                (button) -> ModNetworking.sendToServer(new LevelUpAttributeCS("fitness")), (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            int level = ClientDataStorage.getAttrProperty("fitness", "level");
            int maxLevel = ClientDataStorage.getAttrProperty("fitness", "max");
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = ClientDataStorage.getAttrProperty("fitness", "upgrade");
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));
        //resilience level up
        this.addRenderableWidget(new Button(midX + 95, midY - 15 - 5, 20, 20, Component.literal("+"),
                (button) -> ModNetworking.sendToServer(new LevelUpAttributeCS("resilience")), (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            int level = ClientDataStorage.getAttrProperty("resilience", "level");
            int maxLevel = ClientDataStorage.getAttrProperty("resilience", "max");
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = ClientDataStorage.getAttrProperty("resilience", "upgrade");
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));
        //combat level up
        this.addRenderableWidget(new Button(midX + 95, midY + 15 - 5, 20, 20, Component.literal("+"),
                (button) -> ModNetworking.sendToServer(new LevelUpAttributeCS("combat")), (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            int level = ClientDataStorage.getAttrProperty("combat", "level");
            int maxLevel = ClientDataStorage.getAttrProperty("combat", "max");
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = ClientDataStorage.getAttrProperty("combat", "upgrade");
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));
        //nimbleness level up
        this.addRenderableWidget(new Button(midX + 95, midY + 45 - 5, 20, 20, Component.literal("+"),
                (button) -> ModNetworking.sendToServer(new LevelUpAttributeCS("nimbleness")), (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            int level = ClientDataStorage.getAttrProperty("nimbleness", "level");
            int maxLevel = ClientDataStorage.getAttrProperty("nimbleness", "max");
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = ClientDataStorage.getAttrProperty("nimbleness", "upgrade");
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));
        //strategy level up
        this.addRenderableWidget(new Button(midX + 95, midY + 75 - 5, 20, 20, Component.literal("+"),
                (button) -> ModNetworking.sendToServer(new LevelUpAttributeCS("strategy")), (button, stack, mx, my) -> {
            ArrayList<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Level up").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            int level = ClientDataStorage.getAttrProperty("strategy", "level");
            int maxLevel = ClientDataStorage.getAttrProperty("strategy", "max");
            tooltip.add(Component.literal(level + "/" + maxLevel).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.AQUA));
            if (level != maxLevel) {
                int upgradeCost = ClientDataStorage.getAttrProperty("strategy", "upgrade");
                tooltip.add(Component.literal("Required Points: " + upgradeCost));
            }
            renderTooltip(stack, tooltip, Optional.empty(), mx, my);
        }));
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        RenderSystem.setShaderTexture(0, bg);
        blit(stack, (window.getGuiScaledWidth() - 256) / 2, (window.getGuiScaledHeight() - 256) / 2, 0, 0, 0, 256, 256, 256, 256);

        drawRowForAttribute("Fitness", midY - 45, stack, mouseX, mouseY);
        drawRowForAttribute("Resilience", midY - 15, stack, mouseX, mouseY);
        drawRowForAttribute("Combat", midY + 15, stack, mouseX, mouseY);
        drawRowForAttribute("Nimbleness", midY + 45, stack, mouseX, mouseY);
        drawRowForAttribute("Strategy", midY + 75, stack, mouseX, mouseY);

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

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void drawRowForAttribute(String type, int y, PoseStack stack, int mouseX, int mouseY) {
        float headerX = (float) ((window.getGuiScaledWidth() - 256)/2.0+10);
        this.font.draw(stack, type, headerX, y, 0xff4d4d4d);

        boolean isInWidthRange = mouseX >= (window.getGuiScaledWidth() - 256)/2.0 + 10 && mouseX <= headerX + font.width(type);
        boolean isInHeightRange = mouseY >= y && mouseY <= y + font.lineHeight;

        ArrayList<String> attributeBonuses = new ArrayList<>();
        switch (type) {
            case "Fitness" -> attributeBonuses.add("Health");
            case "Resilience" -> {
                attributeBonuses.add("Armor");
                attributeBonuses.add("Armor Toughness");
            }
            case "Combat" -> {
                attributeBonuses.add("Strength");
                attributeBonuses.add("Parry");
            }
            case "Nimbleness" -> {
                attributeBonuses.add("Speed");
                attributeBonuses.add("Dodge");
            }
            case "Strategy" -> {
                attributeBonuses.add("Reach");
                attributeBonuses.add("Double Strike");
            }
            default -> throw new IllegalArgumentException("Unrecognized Type value while rendering the tooltip for an attribute.");
        }
        if (isInWidthRange && isInHeightRange) {
            ArrayList<Component> tooltipLines = new ArrayList<>();
            tooltipLines.add(Component.literal(type + " Attribute Information").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE));
            for (String bonus:attributeBonuses) {
                tooltipLines.add(Component.literal("-> " + bonus).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.WHITE));
            }
            this.renderTooltip(stack, tooltipLines, java.util.Optional.empty(), mouseX, mouseY, font);
        }

        int currentLevel = ClientDataStorage.getAttrProperty(type, "level");
        int maxLevel = ClientDataStorage.getAttrProperty(type, "max");

        //retrieve attr data from player
        String levelString = currentLevel + "/" + maxLevel;
        this.font.draw(stack, levelString, (float) (midX - this.font.width(levelString)/2.0), y, 0xff4d4d4d);
    }
}

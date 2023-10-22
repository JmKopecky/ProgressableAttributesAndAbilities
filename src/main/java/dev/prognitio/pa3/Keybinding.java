package dev.prognitio.pa3;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybinding {
    //translation key for category
    public static final String CATEGORY = "key.category.pa3.abilitys";
    //translation for the key
    public static final String KEY_ACTIVATE_PRIMARY = "key.pa3.activate_primary";
    public static final String KEY_ACTIVATE_SECONDARY = "key.pa3.activate_secondary";

    public static final KeyMapping PRIMARY_ABILITY_KEY = new KeyMapping(KEY_ACTIVATE_PRIMARY, KeyConflictContext.IN_GAME,
             InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY);
    public static final KeyMapping SECONDARY_ABILITY_KEY = new KeyMapping(KEY_ACTIVATE_SECONDARY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY);
}

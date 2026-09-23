package dev.nativelaunch.client.ui.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.world.entity.player.PlayerSkin;

public class IconDrawUtil {

    /**
     * Draws an authentic pixel-art T-shirt icon (Essential Wardrobe style)
     */
    public static void drawShirt(GuiGraphicsExtractor extractor, int x, int y, int color) {
        // Collar gap at top:
        // . # # . . # # .
        extractor.fill(x + 1, y, x + 3, y + 1, color);
        extractor.fill(x + 5, y, x + 7, y + 1, color);

        // Shoulders & sleeves
        // # # # # # # # #
        extractor.fill(x, y + 1, x + 8, y + 2, color);
        // Sleeves extending down
        extractor.fill(x, y + 2, x + 2, y + 4, color);
        extractor.fill(x + 6, y + 2, x + 8, y + 4, color);

        // Body / Torso
        extractor.fill(x + 2, y + 2, x + 6, y + 8, color);

        // Hemline shadow (subtle darker bottom)
        extractor.fill(x + 2, y + 8, x + 6, y + 9, ColorUtil.withAlpha(color, 180));
    }

    /**
     * Draws an authentic Friends / Social icon (Two player silhouettes)
     */
    public static void drawFriends(GuiGraphicsExtractor extractor, int x, int y, int color) {
        // First person (left, slightly smaller)
        extractor.fill(x + 1, y + 1, x + 4, y + 4, color); // head
        extractor.fill(x, y + 5, x + 5, y + 8, color);     // body

        // Second person (right, main)
        extractor.fill(x + 4, y, x + 8, y + 4, color);     // head
        extractor.fill(x + 3, y + 4, x + 9, y + 8, color); // body
    }

    /**
     * Draws an authentic News / Broadcast antenna icon
     */
    public static void drawNews(GuiGraphicsExtractor extractor, int x, int y, int color) {
        // Center mast
        extractor.fill(x + 3, y + 2, x + 5, y + 8, color);
        extractor.fill(x + 2, y + 7, x + 6, y + 8, color); // base

        // Radio waves (left and right arcs)
        extractor.fill(x + 1, y + 1, x + 2, y + 3, color);
        extractor.fill(x + 6, y + 1, x + 7, y + 3, color);
        extractor.fill(x, y, x + 1, y + 2, color);
        extractor.fill(x + 7, y, x + 8, y + 2, color);
    }

    /**
     * Draws an authentic Screenshot / Picture frame icon
     */
    public static void drawGallery(GuiGraphicsExtractor extractor, int x, int y, int color) {
        // Outer border
        extractor.fill(x, y, x + 9, y + 1, color);
        extractor.fill(x, y + 7, x + 9, y + 8, color);
        extractor.fill(x, y + 1, x + 1, y + 7, color);
        extractor.fill(x + 8, y + 1, x + 9, y + 7, color);

        // Mountain triangles inside
        extractor.fill(x + 2, y + 5, x + 4, y + 7, color);
        extractor.fill(x + 4, y + 4, x + 7, y + 7, color);

        // Sun dot
        extractor.fill(x + 6, y + 2, x + 7, y + 3, color);
    }

    /**
     * Draws an authentic Settings / Sliders mixer icon
     */
    public static void drawSettings(GuiGraphicsExtractor extractor, int x, int y, int color) {
        // Top track & knob
        extractor.fill(x, y + 1, x + 8, y + 2, color);
        extractor.fill(x + 2, y, x + 4, y + 3, color);

        // Middle track & knob
        extractor.fill(x, y + 4, x + 8, y + 5, color);
        extractor.fill(x + 5, y + 3, x + 7, y + 6, color);

        // Bottom track & knob
        extractor.fill(x, y + 7, x + 8, y + 8, color);
        extractor.fill(x + 1, y + 6, x + 3, y + 9, color);
    }

    /**
     * Draws the player's 8x8 skin face directly inside the button
     */
    public static void drawPlayerHead(GuiGraphicsExtractor extractor, int x, int y, int size) {
        Minecraft mc = Minecraft.getInstance();
        PlayerSkin skin = mc.getSkinManager().createLookup(mc.getGameProfile(), true).get();
        if (skin != null) {
            PlayerFaceExtractor.extractRenderState(extractor, skin, x, y, size);
        }
    }
}

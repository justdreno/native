package dev.nativelaunch.client.ui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class VanillaIconButton extends Button {

    @FunctionalInterface
    public interface IconRenderer {
        void render(GuiGraphicsExtractor extractor, int x, int y, boolean hovered);
    }

    private final IconRenderer iconRenderer;

    public VanillaIconButton(int x, int y, int size, Component tooltip, OnPress onPress, IconRenderer iconRenderer) {
        super(x, y, size, size, Component.empty(), onPress, DEFAULT_NARRATION);
        this.iconRenderer = iconRenderer;
        if (tooltip != null) {
            this.setTooltip(Tooltip.create(tooltip));
        }
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        if (this.iconRenderer != null) {
            // Draw icon centered inside the 20x20 button
            int iconX = this.getX() + (this.width - 10) / 2;
            int iconY = this.getY() + (this.height - 10) / 2;
            this.iconRenderer.render(extractor, iconX, iconY, this.isHoveredOrFocused());
        }
    }
}

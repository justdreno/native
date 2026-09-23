package dev.nativelaunch.client.ui.components;

import dev.nativelaunch.client.ui.render.ColorUtil;
import dev.nativelaunch.client.ui.render.Render2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerSkinWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerSkin;

import java.util.function.Supplier;

public class NativePlayerPreview extends NativeWidget {

    private final PlayerSkinWidget skinWidget;

    public NativePlayerPreview(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("Player Preview"), null);

        Minecraft mc = Minecraft.getInstance();
        EntityModelSet modelSet = mc.getEntityModels();
        Supplier<PlayerSkin> skinSupplier = mc.getSkinManager().createLookup(mc.getGameProfile(), true);

        this.skinWidget = new PlayerSkinWidget(width, height, modelSet, skinSupplier);
        this.skinWidget.setX(x);
        this.skinWidget.setY(y);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        if (this.skinWidget != null) {
            this.skinWidget.setX(x);
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        if (this.skinWidget != null) {
            this.skinWidget.setY(y);
        }
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dragX, double dragY) {
        if (this.skinWidget != null) {
            this.skinWidget.mouseDragged(event, dragX, dragY);
        }
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        updateHoverState(delta);

        // 3D Player Model Rendering (Clean, no sci-fi box or glow)
        this.skinWidget.extractRenderState(extractor, mouseX, mouseY, delta);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}

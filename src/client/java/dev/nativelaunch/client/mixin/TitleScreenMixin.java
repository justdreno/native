package dev.nativelaunch.client.mixin;

import dev.nativelaunch.client.ui.components.NativeIconButton;
import dev.nativelaunch.client.ui.components.NativePlayerPreview;
import dev.nativelaunch.client.ui.render.ColorUtil;
import dev.nativelaunch.client.ui.render.Render2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initNativeTitleScreen(CallbackInfo ci) {
        // --- 1. Left Side: 3D Player Model & Wardrobe Action ---
        int playerWidth = 80;
        int playerHeight = 140;
        int playerX = 24;
        int playerY = (this.height / 2) - 80;

        NativePlayerPreview playerPreview = new NativePlayerPreview(playerX, playerY, playerWidth, playerHeight);
        this.addRenderableWidget(playerPreview);

        // Wardrobe quick button directly below 3D player avatar
        int wardrobeBtnSize = 22;
        int wardrobeBtnX = playerX + (playerWidth - wardrobeBtnSize) / 2;
        int wardrobeBtnY = playerY + playerHeight + 4;

        this.addRenderableWidget(new NativeIconButton(
                wardrobeBtnX,
                wardrobeBtnY,
                wardrobeBtnSize,
                "\u2261", // Sleek icon / wardrobe
                Component.literal("Wardrobe & Cosmetics"),
                () -> {
                    // Open wardrobe / skin customization
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new net.minecraft.client.gui.screens.options.SkinCustomizationScreen(this, this.minecraft.options));
                    }
                }
        ));

        // --- 2. Right Side: Vertical Essential-Style Icon Toolbar ---
        int iconSize = 22;
        int rightX = this.width - iconSize - 12;
        int startY = (this.height / 2) - 60;
        int spacing = 26;

        // News Icon
        this.addRenderableWidget(new NativeIconButton(
                rightX, startY, iconSize,
                "\u25CE", // Target / Broadcast icon
                Component.literal("News & Announcements"),
                () -> {}
        ));

        // Friends / Social Icon
        this.addRenderableWidget(new NativeIconButton(
                rightX, startY + spacing, iconSize,
                "\u263A", // Smiley / Social
                Component.literal("Friends & Social"),
                () -> {}
        ));

        // Wardrobe Icon
        this.addRenderableWidget(new NativeIconButton(
                rightX, startY + (spacing * 2), iconSize,
                "\u2606", // Star / Cosmetics
                Component.literal("Wardrobe & Outfits"),
                () -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new net.minecraft.client.gui.screens.options.SkinCustomizationScreen(this, this.minecraft.options));
                    }
                }
        ));

        // Screenshots Gallery Icon
        this.addRenderableWidget(new NativeIconButton(
                rightX, startY + (spacing * 3), iconSize,
                "\u25A3", // Gallery / Frame icon
                Component.literal("Screenshots & Gallery"),
                () -> {}
        ));

        // Native Settings Icon
        this.addRenderableWidget(new NativeIconButton(
                rightX, startY + (spacing * 4), iconSize,
                "\u2699", // Gear / Settings icon
                Component.literal("Native Client Settings"),
                () -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new net.minecraft.client.gui.screens.options.OptionsScreen(this, this.minecraft.options));
                    }
                }
        ));
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderNativeTitleScreenOverlay(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        // 1. Current Account Bar (Center Bottom, as in Essential Mod)
        String username = mc.getUser() != null ? mc.getUser().getName() : "Player";
        String accountText = "Current Account: " + username;
        int centerX = this.width / 2;
        int bottomY = this.height - 24;

        // Subtle glow backdrop for account text
        int textWidth = mc.font.width(accountText);
        Render2D.drawGlassPanel(extractor, centerX - (textWidth / 2) - 8, bottomY - 3, textWidth + 16, 14, 3, ColorUtil.rgba(15, 15, 20, 140), ColorUtil.rgba(255, 140, 66, 80));
        Render2D.drawCenteredString(extractor, mc.font, accountText, centerX, bottomY, ColorUtil.ACCENT_ORANGE);

        // 2. Branding watermark on Bottom Left
        String branding = "Native Client v1.0.0";
        Render2D.drawString(extractor, mc.font, branding, 6, this.height - 12, ColorUtil.ACCENT_CYAN);
    }
}

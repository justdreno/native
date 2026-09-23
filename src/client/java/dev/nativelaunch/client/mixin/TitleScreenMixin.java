package dev.nativelaunch.client.mixin;

import dev.nativelaunch.client.ui.components.NativePlayerPreview;
import dev.nativelaunch.client.ui.components.VanillaIconButton;
import dev.nativelaunch.client.ui.render.IconDrawUtil;
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
        // --- 1. Left Side: Natural 3D Player Avatar & Wardrobe Button ---
        int playerWidth = 65;
        int playerHeight = 125;
        int playerX = 38;
        // Position player standing naturally towards bottom
        int playerY = Math.max(10, this.height - 165);

        NativePlayerPreview playerPreview = new NativePlayerPreview(playerX, playerY, playerWidth, playerHeight);
        this.addRenderableWidget(playerPreview);

        // Vanilla-style 20x20 Wardrobe Button directly below the player (like Essential)
        int wardrobeSize = 20;
        int wardrobeX = playerX + (playerWidth - wardrobeSize) / 2;
        int wardrobeY = this.height - 35;

        this.addRenderableWidget(new VanillaIconButton(
                wardrobeX,
                wardrobeY,
                wardrobeSize,
                Component.literal("Wardrobe"),
                btn -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new net.minecraft.client.gui.screens.options.SkinCustomizationScreen(this, this.minecraft.options));
                    }
                },
                (extractor, x, y, hovered) -> {
                    int color = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                    IconDrawUtil.drawShirt(extractor, x + 1, y, color);
                }
        ));

        // --- 2. Right Side: Vertical 6-Button Toolbar (Authentic Minecraft Buttons) ---
        int btnSize = 20;
        int rightX = this.width - btnSize - 4;
        int totalHeight = (6 * btnSize) + (5 * 2); // 6 buttons + 2px gaps
        int startY = (this.height - totalHeight) / 2;
        int step = btnSize + 2;

        // 1. News Icon
        this.addRenderableWidget(new VanillaIconButton(
                rightX, startY, btnSize,
                Component.literal("News"),
                btn -> {},
                (extractor, x, y, hovered) -> {
                    int color = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                    IconDrawUtil.drawNews(extractor, x + 1, y + 1, color);
                }
        ));

        // 2. Friends / Social Icon
        this.addRenderableWidget(new VanillaIconButton(
                rightX, startY + step, btnSize,
                Component.literal("Friends"),
                btn -> {},
                (extractor, x, y, hovered) -> {
                    int color = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                    IconDrawUtil.drawFriends(extractor, x + 1, y + 1, color);
                }
        ));

        // 3. Wardrobe / Cosmetics Icon
        this.addRenderableWidget(new VanillaIconButton(
                rightX, startY + (step * 2), btnSize,
                Component.literal("Wardrobe"),
                btn -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new net.minecraft.client.gui.screens.options.SkinCustomizationScreen(this, this.minecraft.options));
                    }
                },
                (extractor, x, y, hovered) -> {
                    int color = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                    IconDrawUtil.drawShirt(extractor, x + 1, y + 1, color);
                }
        ));

        // 4. Screenshots / Gallery Icon
        this.addRenderableWidget(new VanillaIconButton(
                rightX, startY + (step * 3), btnSize,
                Component.literal("Screenshots"),
                btn -> {},
                (extractor, x, y, hovered) -> {
                    int color = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                    IconDrawUtil.drawGallery(extractor, x + 1, y + 1, color);
                }
        ));

        // 5. Settings / Mod Options Icon
        this.addRenderableWidget(new VanillaIconButton(
                rightX, startY + (step * 4), btnSize,
                Component.literal("Native Settings"),
                btn -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreenAndShow(new net.minecraft.client.gui.screens.options.OptionsScreen(this, this.minecraft.options));
                    }
                },
                (extractor, x, y, hovered) -> {
                    int color = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                    IconDrawUtil.drawSettings(extractor, x + 1, y + 1, color);
                }
        ));

        // 6. Account / Profile Face Icon
        this.addRenderableWidget(new VanillaIconButton(
                rightX, startY + (step * 5), btnSize,
                Component.literal("Account Profile"),
                btn -> {},
                (extractor, x, y, hovered) -> {
                    // Draw 8x8 player face centered
                    IconDrawUtil.drawPlayerHead(extractor, x + 1, y + 1, 8);
                }
        ));
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderNativeTitleScreenOverlay(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        // 1. Current Account Text (Centered directly below Options / Quit row, exactly like Essential)
        String username = mc.getUser() != null ? mc.getUser().getName() : "Player";
        String accountText = "Current Account: " + username;
        int textWidth = mc.font.width(accountText);
        int centerX = (this.width - textWidth) / 2;

        // Standard Options button row is at: this.height / 4 + 48 + 72 + 12 (height = 20)
        // We place this cleanly 7px below the row:
        int accountY = (this.height / 4 + 132) + 20 + 7;

        // Clean text with drop shadow (Warm coral color #E58B68) - NO sci-fi box
        extractor.text(mc.font, accountText, centerX, accountY, 0xFFE58B68, true);

        // 2. Client Branding on Bottom Left (Cleanly above Minecraft version to prevent collision)
        String branding = "Native Client v1.0.0";
        extractor.text(mc.font, branding, 2, this.height - 20, 0xFFAAAAAA, true);
    }
}

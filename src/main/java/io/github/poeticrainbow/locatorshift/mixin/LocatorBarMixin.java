package io.github.poeticrainbow.locatorshift.mixin;

import io.github.poeticrainbow.locatorshift.LocatorShiftConfig;
import io.github.poeticrainbow.locatorshift.ShiftedLocatorBar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocatorBarRenderer.class)
public abstract class LocatorBarMixin implements ContextualBarRenderer {
    @Unique
    private static final Minecraft client = Minecraft.getInstance();

    @Inject(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    private void locatorshift$toggle_locator_bar_visibility(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        // Cancel rendering when not visible
        if (!shouldRender()) ci.cancel();
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    private void locatorshift$toggle_locator_bar_addon_visibility(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        // Cancel rendering when not visible
        if (!shouldRender()) ci.cancel();
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("TAIL"))
    private void locatorshift$render_player_heads(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        if (!LocatorShiftConfig.renderPlayerHeads) return;
        if (client.player == null) return;
        // Render player heads over normal locator bar
        int y = top(client.getWindow());
        client.player.connection.getWaypointManager().forEachWaypoint(client.getCameraEntity(), waypoint -> {
            ShiftedLocatorBar.renderWaypointAsPlayerHead(context, waypoint, y);
        });
    }

    @Unique
    private static boolean shouldRender() {
        return LocatorShiftConfig.locatorBarVisible;
    }
}

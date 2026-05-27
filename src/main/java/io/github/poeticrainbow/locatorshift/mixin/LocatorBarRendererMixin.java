package io.github.poeticrainbow.locatorshift.mixin;

import io.github.poeticrainbow.locatorshift.LocatorShiftConfig;
import io.github.poeticrainbow.locatorshift.ShiftedLocatorBar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocatorBarRenderer.class)
public abstract class LocatorBarRendererMixin implements ContextualBarRenderer {
    @Unique
    private static final Minecraft client = Minecraft.getInstance();

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void locatorshift$toggle_locator_bar_visibility(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        // Cancel rendering when not visible
        if (!shouldRender()) ci.cancel();
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void locatorshift$toggle_locator_bar_addon_visibility(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        // Cancel rendering when not visible
        if (!shouldRender()) ci.cancel();
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void locatorshift$render_player_heads(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!LocatorShiftConfig.renderPlayerHeads) return;
        if (client.player == null) return;
        var camera = client.getCameraEntity();
        if (camera == null) return;
        // Render player heads over normal locator bar
        int y = top(client.getWindow());
        client.player.connection.getWaypointManager().forEachWaypoint(camera, waypoint -> {
            ShiftedLocatorBar.renderWaypointAsPlayerHead(graphics, waypoint, y);
        });
    }

    @Unique
    private static boolean shouldRender() {
        return LocatorShiftConfig.locatorBarVisible;
    }
}

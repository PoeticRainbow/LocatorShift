package io.github.poeticrainbow.locatorshift.mixin;

import io.github.poeticrainbow.locatorshift.LocatorShiftConfig;
import io.github.poeticrainbow.locatorshift.ShiftedLocatorBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocatorBar.class)
public abstract class LocatorBarMixin implements Bar {
    @Unique
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void locatorshift$toggle_locator_bar_visibility(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        // Cancel rendering when not visible
        if (!shouldRender()) ci.cancel();
    }

    @Inject(method = "renderAddons(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void locatorshift$toggle_locator_bar_addon_visibility(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        // Cancel rendering when not visible
        if (!shouldRender()) ci.cancel();
    }

    @Inject(method = "renderAddons(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("TAIL"))
    private void locatorshift$render_player_heads(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!LocatorShiftConfig.renderPlayerHeads) return;
        if (client.player == null) return;
        // Render player heads over normal locator bar
        int y = getCenterY(client.getWindow());
        client.player.networkHandler.getWaypointHandler().forEachWaypoint(client.getCameraEntity(), waypoint -> {
            ShiftedLocatorBar.renderWaypointAsPlayerHead(context, waypoint, y);
        });
    }

    @Unique
    private static boolean shouldRender() {
        return LocatorShiftConfig.locatorBarVisible;
    }
}

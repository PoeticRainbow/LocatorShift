package io.github.poeticrainbow.locatorshift;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;
import java.util.UUID;

public class ShiftedLocatorBar extends LocatorBar {
    protected final MinecraftClient client;

    public ShiftedLocatorBar(MinecraftClient client) {
        super(client);
        this.client = client;
    }

    @Override
    public int getCenterY(Window window) {
        return getCenterY();
    }

    public int getCenterY() {
        return LocatorShiftConfig.yShift;
    }

    @Override
    public void renderBar(DrawContext context, RenderTickCounter tickCounter) {
        if (shouldRender()) super.renderBar(context, tickCounter);
    }

    @Override
    public void renderAddons(DrawContext context, RenderTickCounter tickCounter) {
        if (!shouldRender()) return;
        super.renderAddons(context, tickCounter);

        if (!LocatorShiftConfig.renderPlayerHeads) return;
        if (client.player == null) return;
        if (client.world == null) return;

        this.client.player.networkHandler.getWaypointHandler().forEachWaypoint(client.cameraEntity, waypoint -> {
            var source = waypoint.getSource().left();
            if (source.isPresent()) {
                var uuid = source.get();
                var entry = getEntryForWaypoint(uuid);
                if (entry.isEmpty()) return;
                var skin = entry.get().getSkinTextures();

                double relativeYaw = waypoint.getRelativeYaw(client.world, this.client.gameRenderer.getCamera());
                if (relativeYaw < -61.0d || relativeYaw > 61.0d) return;
                int leftEdge = MathHelper.ceil((float)(context.getScaledWindowWidth() - 9) / 2.0F);
                int xOffset = (int)(relativeYaw * 173.0d / 2.0d / 60.0d);
                var distance = waypoint.squaredDistanceTo(client.cameraEntity);
                renderPlayerHead(context, skin, leftEdge + xOffset, 255 - (int) Math.sqrt(distance) / 3);
            }
        });
    }

    public Optional<PlayerListEntry> getEntryForWaypoint(UUID uuid) {
        var handler = client.getNetworkHandler();
        if (handler == null) return Optional.empty();
        var entry = handler.getPlayerListEntry(uuid);
        if (entry == null) return Optional.empty();
        return Optional.of(entry);
    }

    public void renderPlayerHead(DrawContext context, SkinTextures texture, int x, int distance) {
        distance = Math.clamp(distance, 50, 255);
        PlayerSkinDrawer.draw(context, texture.texture(), x, getCenterY() - 2, 8, true, false,
            ColorHelper.getArgb(255, distance, distance, distance));
    }

    public boolean shouldRender() {
        var handler = client.getNetworkHandler();
        if (handler == null) return false;
        return LocatorShiftConfig.locatorBarVisible && LocatorShiftConfig.shiftLocatorBar && client.getNetworkHandler().getWaypointHandler().hasWaypoint();
    }
}

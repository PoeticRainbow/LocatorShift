package io.github.poeticrainbow.locatorshift;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

import static io.github.poeticrainbow.locatorshift.LocatorShiftConfig.*;

public class ShiftedLocatorBar extends LocatorBarRenderer {
    public static final Minecraft client = Minecraft.getInstance();

    public ShiftedLocatorBar(Minecraft client) {
        super(client);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        if (isVisible()) super.extractBackground(graphics, deltaTracker);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        if (isVisible()) super.extractRenderState(graphics, deltaTracker);
    }

    @Override
    public int top(@NonNull Window window) {
        return yShift;
    }

    public static void renderWaypointAsPlayerHead(GuiGraphicsExtractor context, TrackedWaypoint waypoint, int y) {
        if (client.level == null) return;
        var camera = client.getCameraEntity();
        if (camera == null) return;
        var source = waypoint.id().left();
        if (source.isPresent()) {
            var uuid = source.get();
            var entry = getEntryForWaypoint(uuid);
            if (entry.isEmpty()) return;
            var skin = entry.get().getSkin();

            double relativeYaw = waypoint.yawAngleToCamera(client.level, client.gameRenderer.getMainCamera(), entity -> 0f);
            if (relativeYaw < -61.0d || relativeYaw > 61.0d) return;
            int leftEdge = Mth.ceil((float) (context.guiWidth() - 9) / 2.0F);
            int xOffset = (int) (relativeYaw * 173.0d / 2.0d / 60.0d);
            var distance = (int) Math.sqrt(waypoint.distanceSquared(camera));
            var brightness = darkenWithDistance ? Math.clamp((float) (distance - far) / (near - far), 0, 1) : 1;
            var scale = shrinkWhenFar ? (distance > far ? 5 : 8) : 8;
            extractPlayerHead(context, skin, leftEdge + xOffset + 4, y + 2, (int) (brightness * 255), scale);
        }
    }

    public static Optional<PlayerInfo> getEntryForWaypoint(UUID uuid) {
        var handler = client.getConnection();
        if (handler == null) return Optional.empty();
        var entry = handler.getPlayerInfo(uuid);
        if (entry == null) return Optional.empty();
        return Optional.of(entry);
    }

    private static void extractPlayerHead(GuiGraphicsExtractor context, PlayerSkin texture, int x, int y, int brightness, int scale) {
        brightness = Math.clamp(brightness, minimumBrightness, 255);
        var centerOffset = scale / 2;

        PlayerFaceExtractor.extractRenderState(
            context, texture.body().texturePath(), x - centerOffset, y - centerOffset, scale, true, false,
            ARGB.color(255, brightness, brightness, brightness)
        );
    }

    public static boolean isVisible() {
        var handler = client.getConnection();
        if (handler == null) return false;
        return shiftLocatorBar && client.getConnection().getWaypointManager().hasWaypoints();
    }
}

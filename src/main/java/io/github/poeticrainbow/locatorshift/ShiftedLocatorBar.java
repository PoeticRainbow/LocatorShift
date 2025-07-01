package io.github.poeticrainbow.locatorshift;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;

public class ShiftedLocatorBar extends LocatorBar {
    protected final MinecraftClient client;
    public static boolean enabled = true;

    public ShiftedLocatorBar(MinecraftClient client) {
        super(client);
        this.client = client;
    }

    @Override
    public int getCenterY(Window window) {
        return 4;
    }

    @Override
    public void renderBar(DrawContext context, RenderTickCounter tickCounter) {
        if (shouldRender()) super.renderBar(context, tickCounter);
    }

    @Override
    public void renderAddons(DrawContext context, RenderTickCounter tickCounter) {
        if (shouldRender()) super.renderAddons(context, tickCounter);
    }

    public boolean shouldRender() {
        var handler = client.getNetworkHandler();
        if (handler == null) return false;
        return client.getNetworkHandler().getWaypointHandler().hasWaypoint() && enabled;
    }
}

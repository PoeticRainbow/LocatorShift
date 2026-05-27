package io.github.poeticrainbow.locatorshift;

import com.mojang.blaze3d.platform.InputConstants;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocatorShift implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "locatorshift";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(of(MOD_ID));
    public static final KeyMapping TOGGLE_LOCATOR_BAR = KeyBindingHelper.registerKeyBinding(
        new KeyMapping(
            "key.locatorshift.toggle_locator_bar",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_F10,
            CATEGORY
        ));

    @Override
    public void onInitialize() {
        MidnightConfig.init(MOD_ID, LocatorShiftConfig.class);
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Shifting that Locator Bar!");
        var locatorBar = new ShiftedLocatorBar(Minecraft.getInstance());

        //// Background renders behind the Boss Bar and Text
        HudElementRegistry.attachElementBefore(VanillaHudElements.BOSS_BAR, of("locator_bar"), locatorBar::renderBackground);
        //// Addons render in front of the Boss Bar and Text
        HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, of("locator_bar_addons"), locatorBar::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (TOGGLE_LOCATOR_BAR.consumeClick()) {
                LocatorShiftConfig.locatorBarVisible = !LocatorShiftConfig.locatorBarVisible;
                LocatorShiftConfig.write(MOD_ID);

                client.player.displayClientMessage(Component.translatable(LocatorShiftConfig.locatorBarVisible ?
                    "message.locatorshift.toggle_on" : "message.locatorshift.toggle_off"), true);
            }
        });
    }

    public static Identifier of(String value) {
        return Identifier.fromNamespaceAndPath(MOD_ID, value);
    }
}

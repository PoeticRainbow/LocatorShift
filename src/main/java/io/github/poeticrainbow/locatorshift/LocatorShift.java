package io.github.poeticrainbow.locatorshift;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocatorShift implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "locatorshift";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyBinding TOGGLE_LOCATOR_BAR = KeyBindingHelper.registerKeyBinding(
        new KeyBinding(
            "key.locatorshift.toggle_locator_bar",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_F10,
            "category.locatorshift.locatorshift"
        ));

    @Override
    public void onInitialize() {
        MidnightConfig.init(MOD_ID, LocatorShiftConfig.class);
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Shifting that Locator Bar!");
        var locatorBar = new ShiftedLocatorBar(MinecraftClient.getInstance());

        //// Background renders behind the Boss Bar and Text
        HudElementRegistry.attachElementBefore(VanillaHudElements.BOSS_BAR, of("locator_bar"), locatorBar::renderBar);
        //// Addons render in front of the Boss Bar and Text
        HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, of("locator_bar_addons"), locatorBar::renderAddons);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && TOGGLE_LOCATOR_BAR.wasPressed()) {
                LocatorShiftConfig.locatorBarVisible = !LocatorShiftConfig.locatorBarVisible;
                LocatorShiftConfig.write(MOD_ID);

                client.player.sendMessage(Text.translatable(LocatorShiftConfig.locatorBarVisible ?
                    "message.locatorshift.toggle_on" : "message.locatorshift.toggle_off"), true);
            }
        });
    }

    public static Identifier of(String value) {
        return Identifier.of(MOD_ID, value);
    }
}

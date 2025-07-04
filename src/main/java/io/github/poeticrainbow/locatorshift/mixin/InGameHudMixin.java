package io.github.poeticrainbow.locatorshift.mixin;

import io.github.poeticrainbow.locatorshift.LocatorShiftConfig;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @ModifyVariable(method = "getCurrentBarType()Lnet/minecraft/client/gui/hud/InGameHud$BarType;", at = @At("STORE"), ordinal = 0)
    private boolean locatorshift$disable_default_locator_bar(boolean value) {
        // true means show locator, false means hide locator bar
        return !LocatorShiftConfig.shiftLocatorBar && LocatorShiftConfig.locatorBarVisible;
    }
}

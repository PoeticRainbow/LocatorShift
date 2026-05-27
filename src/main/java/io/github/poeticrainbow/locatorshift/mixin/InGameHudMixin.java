package io.github.poeticrainbow.locatorshift.mixin;

import io.github.poeticrainbow.locatorshift.LocatorShiftConfig;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class InGameHudMixin {
    @ModifyVariable(method = "nextContextualInfoState()Lnet/minecraft/client/gui/Gui$ContextualInfo;", at = @At("STORE"), ordinal = 0)
    private boolean locatorshift$disable_default_locator_bar(boolean value) {
        // true means show locator, false means hide locator bar
        return !LocatorShiftConfig.shiftLocatorBar && LocatorShiftConfig.locatorBarVisible;
    }
}

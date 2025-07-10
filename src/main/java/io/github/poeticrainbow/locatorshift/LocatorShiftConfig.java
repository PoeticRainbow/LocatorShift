package io.github.poeticrainbow.locatorshift;

import eu.midnightdust.lib.config.MidnightConfig;

public class LocatorShiftConfig extends MidnightConfig {
    public static final String OPTIONS = "Options";

    @Entry(category = OPTIONS) public static boolean locatorBarVisible = true;
    @Entry(category = OPTIONS) public static boolean shiftLocatorBar = true;
    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Entry(category = OPTIONS, min = 0) public static int yShift = 4;

    @Entry(category = OPTIONS) public static boolean renderPlayerHeads = true;
    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Entry(category = OPTIONS, isSlider = true, min = 0, max = 4096) public static int near = 128;
    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Entry(category = OPTIONS, isSlider = true, min = 0, max = 4096) public static int far = 1024;

    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Entry(category = OPTIONS) public static boolean shrinkWhenFar = false;

    @Entry(category = OPTIONS) public static boolean darkenWithDistance = true;
    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Condition(requiredOption = "darkenWithDistance", requiredValue = "true")
    @Entry(category = OPTIONS, min = 0, max = 255, isSlider = true, precision = 1) public static int minimumBrightness = 50;
}

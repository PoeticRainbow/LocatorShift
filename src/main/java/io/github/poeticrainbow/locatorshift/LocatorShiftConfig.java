package io.github.poeticrainbow.locatorshift;

import eu.midnightdust.lib.config.MidnightConfig;

public class LocatorShiftConfig extends MidnightConfig {
    public static final String OPTIONS = "Options";

    @Entry(category = OPTIONS) public static boolean locatorBarVisible = true;
    @Entry(category = OPTIONS) public static boolean shiftLocatorBar = true;
    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Entry(category = OPTIONS) public static boolean renderPlayerHeads = true;
    @Condition(requiredOption = "shiftLocatorBar", requiredValue = "true")
    @Entry(category = OPTIONS, min = 0) public static int yShift = 4;
}

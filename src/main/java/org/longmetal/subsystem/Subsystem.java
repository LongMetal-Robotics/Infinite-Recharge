package org.longmetal.subsystem;

import org.longmetal.util.Console;

public class Subsystem {
    protected static boolean enabled = false;
    protected boolean initialized = false;
    protected static boolean disabledErrorThrown = false;

    /**
     * Creates a Subsystem
     *
     * @param isEnabled determines if the subsystem will be enabled on startup
     */
    public Subsystem(boolean isEnabled) {
        enabled = isEnabled;
        if (enabled) {
            init();
        } else {
            Console.warn(
                    this.getClass().getSimpleName()
                            + " wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    /**
     * Initialize the subsystem. NOTE: YOU MUST CALL THIS METHOD USING {@code super.init()} AFTER
     * THE OVERRIDDEN {@code init()} METHOD
     */
    public void init() {
        if (!initialized) {
            initialized = true;
        }
    }

    /**
     * Enable or disable the subsystem
     *
     * @param isEnabled
     */
    public void setEnabled(boolean isEnabled) {
        staticSetEnabled(isEnabled);
        if (isEnabled && !initialized) {
            init();
        }
    }

    public static void staticSetEnabled(boolean isEnabled) {
        if (isEnabled) {
            disabledErrorThrown = false;
        }
        enabled = isEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

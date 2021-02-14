package org.longmetal.subsystem;

public class Subsystem {
    protected static boolean enabled = false;
    protected static boolean initialized = false;
    protected static boolean disabledErrorThrown = false;

    /**
     * Initialize the subsystem. NOTE: YOU MUST CALL THIS METHOD USING {@code super.init()} AFTER
     * THE OVERRIDDEN {@code init()} METHOD
     */
    public static void init() {
        if (!initialized) {
            initialized = true;
        }
    }

    /**
     * Enable or disable the subsystem
     *
     * @param isEnabled
     */

    public static void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            disabledErrorThrown = false;
        }
        enabled = isEnabled;
        if (isEnabled && !initialized) {
            init();
        }
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

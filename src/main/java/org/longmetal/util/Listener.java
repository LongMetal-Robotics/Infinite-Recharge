package org.longmetal.util;

public class Listener {
    private boolean oldValue;
    private Runnable onTrue;
    private Runnable onFalse;

    /**
     * Create a Listener
     *
     * @param onTrue is called during the {@link #update(boolean) update} method if the value
     *     changes to true
     * @param onFalse is called during the {@link #update(boolean) update} method if the value
     *     changes to false
     */
    public Listener(Runnable onTrue, Runnable onFalse) {
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    /**
     * Listen for a change in the value. Runs the appropriate Runnable if the value changed
     *
     * @param newValue is the value to check for a change
     * @return a boolean which is true if a Runnable was run
     */
    public boolean update(boolean newValue) {
        boolean somethingRan = true;
        if (oldValue != newValue) { // If the value changed
            if (newValue && onTrue != null) { // If the new value is true
                onTrue.run();
            } else if (!newValue && onFalse != null) { // If the new value is false
                onFalse.run();
            } else {
                somethingRan = false;
            }
        } else {
            somethingRan = false;
        }
        oldValue = newValue; // Save the new value to compare the next time
        return somethingRan;
    }
}

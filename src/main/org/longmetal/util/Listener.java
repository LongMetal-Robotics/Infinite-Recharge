package org.longmetal.util;

public class Listener {
    private boolean oldValue = false;
    private Runnable onTrue;
    private Runnable onFalse;

    /**
     * Create a Listener with an initial value of false
     * @param onTrue is called during the {@link #update(boolean) update} method if the value changes to true
     * @param onFalse is called during the {@link #update(boolean) update} method if the value changes to false
     */
    public Listener(Runnable onTrue, Runnable onFalse) {
        this(onTrue, onFalse, false);
    }

    /**
     * Create a Listener
     * @param onTrue is called during the {@link #update(boolean) update} method if the value changes to true
     * @param onFalse is called during the {@link #update(boolean) update} method if the value changes to false
     * @param initValue is the value initally stored; it is what is the new value is compared to in the {@link #update(boolean) update} method
     */
    public Listener(Runnable onTrue, Runnable onFalse, boolean initValue) {
        this.onTrue = onTrue;
        this.onFalse = onFalse;
        oldValue = initValue;
    }

    /**
     * Listen for a change in the value. Runs the appropriate Runnable if the value changed
     * @param newValue is the value to check for a change
     * @return a boolean which is true if a Runnable was run
     */
    public boolean update(boolean newValue) {
        boolean somethingRan = false;
        if (oldValue != newValue) { // If the value changed
            somethingRan = true;
            if (newValue) { // If the new value is true
                if (onTrue != null) {
                    onTrue.run();
                } else {
                    somethingRan = false;
                }
            } else {        // If the new value is false
                if (onFalse != null) {
                    onFalse.run();
                } else {
                    somethingRan = false;
                }
            }
        }
        oldValue = newValue;    // Save the new value to compare the next time
        return somethingRan;
    }
}
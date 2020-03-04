package org.longmetal.util;

public class Delay {
    private static boolean enabled = false;

    public static void setEnabled(boolean enabled) {
        Delay.enabled = enabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }

    /**
     * Delay running a Runnable for a certain amount of time.
     * If {@code setEnabled(false)} is called within the delay, the runnable will not run.
     * 
     * @param exec  The Runnable to delay
     * @param delay The time to delay running it (in ms)
     */
    public static void delay(Runnable exec, int delay) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    if (Delay.enabled) {
                        exec.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
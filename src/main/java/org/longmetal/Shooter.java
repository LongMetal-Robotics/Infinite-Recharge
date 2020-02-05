package org.longmetal;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter {
    private CANSparkMax drum;
    private static boolean enabled = true;
    private boolean initialized = false;

    public Shooter(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println(
                    "[WARN]\tShooter wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        drum = new CANSparkMax(Constants.kP_DRUM, MotorType.kBrushless);
        initialized = true;
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
                drum.set(0);
            }
        }
    }
}

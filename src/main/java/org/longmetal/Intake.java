package org.longmetal;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake {
    private TalonSRX intakeBoi;
    private TalonSRX transportBoi;

    private static boolean enabled = true;
    private boolean initialized = false;

    public Intake() {
        this(true);
    }

    public Intake(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println(
                    "[WARN]\tShooter wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        intakeBoi = new TalonSRX(Constants.kP_INTAKE);
        transportBoi = new TalonSRX(Constants.kP_TRANSPORT);

        initialized = true;
    }
}

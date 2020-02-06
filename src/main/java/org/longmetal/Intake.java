package org.longmetal;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake {
    private TalonSRX mIntake; // Thanks for the wonderful variable names Jon, I fixed them :)
    private TalonSRX mTransport;

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
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mTransport = new TalonSRX(Constants.kP_TRANSPORT);

        initialized = true;
    }
}

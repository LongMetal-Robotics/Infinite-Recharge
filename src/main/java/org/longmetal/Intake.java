package org.longmetal;

import org.longmetal.exception.*;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {
    private TalonSRX mIntake; // Thanks for the wonderful variable names Jon, I fixed them :)
    private CANSparkMax mTransport;

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
        mTransport = new CANSparkMax(Constants.kP_TRANSPORT, MotorType.kBrushless);
        mTransport.setOpenLoopRampRate(1);
        initialized = true;
    }

    public void setMotor(double speed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        mTransport.set(speed);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
            }
            mTransport.set(0);
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

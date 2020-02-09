package org.longmetal;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.longmetal.exception.*;

public class Intake {
    private TalonSRX mIntake; // mIntake is the intake itself
    private CANSparkMax mTransport; // mTransport drives the internal ramp

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
                    "[WARN]\tIntake wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mTransport = new CANSparkMax(Constants.kP_TRANSPORT, MotorType.kBrushless);
        mTransport.setOpenLoopRampRate(1); // Just for testing purposes
        initialized = true;
    }

    public void setTransportSpeed(double speed) throws SubsystemException {
        // SubsystemManager.check(enabled, initialized);
        mTransport.set(speed);
    }

    public void setIntakeSpeed(double speed) throws SubsystemException {
        // SubsystemManager.check(enabled, initialized);
        mIntake.set(ControlMode.PercentOutput, speed);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
            }
            mTransport.set(0); // Sets mTransport to a speed of 0
            mTransport.setOpenLoopRampRate(1); // Just for testing purposes
            mIntake.set(ControlMode.PercentOutput, 0); // Sets mIntake to a speed of 0
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

package org.longmetal;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.longmetal.exception.*;

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
                    "[WARN]\tIntake wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mTransport = new CANSparkMax(Constants.kP_TRANSPORT, MotorType.kBrushless);
        initialized = true;
    }

    public void setTransportSpeed(double speed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        mTransport.set(speed);
    }

    public void setIntakeSpeed(double speed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        mIntake.set(ControlMode.PercentOutput, speed);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
            }
            mTransport.set(0);
            mTransport.setOpenLoopRampRate(1);
            mIntake.set(ControlMode.PercentOutput, 0);
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

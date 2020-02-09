package org.longmetal;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.longmetal.util.LMMath;
import org.longmetal.exception.SubsystemException;

public class Shooter {
    private CANSparkMax mShooter;
    private TalonSRX mSingulator; // this is the motor that has the mec wheels attached
    private static boolean enabled = true;
    private boolean initialized = false;
    private boolean shooting = false;
    private double modifierX = 0;
    private double modifierY = 0;

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
        mShooter = new CANSparkMax(Constants.kP_SHOOTER, MotorType.kBrushless);
        mSingulator = new TalonSRX(Constants.kP_SINGULATOR);
        // mShooter.setOpenLoopRampRate(1);
        initialized = true;
    }

    /*public void setSingFreq(double singNum) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        mSingulator.set(Math.limit(singNum, Constants.kSINGULATOR_MIN, Constants.kSINGULATOR_MAX).doubleValue());
    }*/

    public void setShootSpeed(double shootSpeed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        shootSpeed = LMMath.limit(shootSpeed, Constants.kSHOOTER_MIN, Constants.kSHOOTER_MAX).doubleValue();
        mShooter.set(shootSpeed);
        shooting = shootSpeed > Constants.kSHOOTER_MIN + 0.1;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void modifier(double modifierX, double modifierY) {
        this.modifierX = modifierX;
        this.modifierY = modifierY;
    }

    public void run(double speed) throws SubsystemException {
        run(speed, true);
    }

    public void run(double speed, boolean runSingulator) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        if (runSingulator) {
            mSingulator.set(ControlMode.PercentOutput, speed);
        }
        speed *= Constants.kSHOOTER_SPEED_MODIFIER;
        double xModifier = modifierX * Constants.kSHOOTER_X_MODIFIER;
        double yModifier = modifierY * Constants.kSHOOTER_Y_MODIFIER;
        mShooter.set((double)LMMath.limit(speed - xModifier + yModifier, Constants.kSHOOTER_MIN, Constants.kSHOOTER_MAX));
    }

    public void idle() throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        mSingulator.set(ControlMode.PercentOutput, 0);
        run(Constants.kSHOOTER_MIN, false);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();  
            }
            mShooter.set(0);
            mSingulator.set(ControlMode.PercentOutput, 0);
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

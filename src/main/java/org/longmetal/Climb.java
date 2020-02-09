package org.longmetal;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.longmetal.exception.SubsystemException;

import edu.wpi.first.wpilibj.Solenoid;

public class Climb {
    private CANSparkMax mWinch1;
    private CANSparkMax mWinch2;
    private Solenoid drumSpin1;
    private Solenoid drumSpin2;
    private static boolean enabled = true;
    private boolean initialized = false;

    public Climb(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println(
                    "[WARN]\tClimb wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        mWinch1 = new CANSparkMax(Constants.kP_WINCH1, MotorType.kBrushless);
        mWinch2 = new CANSparkMax(Constants.kP_WINCH2, MotorType.kBrushless);
        mWinch1.setOpenLoopRampRate(1); // for testing only
        mWinch2.setOpenLoopRampRate(1); // for testing only
        drumSpin1 = new Solenoid(Constants.kC_CLIMB1);
        drumSpin2 = new Solenoid(Constants.kC_CLIMB2);
        initialized = true;
    }

    public void setWinchSpeed(double speed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        mWinch1.set(speed);
        mWinch2.set(speed);
    }


    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
                mWinch1.set(0);
                mWinch2.set(0);
            }
        }
    }
}

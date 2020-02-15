package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.longmetal.Constants;

public class Intake extends Subsystem {
    private TalonSRX mIntake;
    private TalonSRX mHopper;

    public Intake(boolean setEnabled) {
        super(setEnabled);
        if (setEnabled) { // I'm dumb and they way I wrote the inheritance, it wouldn't work (it
                          // wouldn't call the right init).
            init();
        }
    }

    public void init() {
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mHopper = new TalonSRX(Constants.kP_HOPPER);

        super.init();
    }

    public void setIntakeSpeed(double rTrigger) {
        mIntake.set(ControlMode.PercentOutput, rTrigger);
    }

    public void setHopperSpeed(double d) {
        mHopper.set(ControlMode.PercentOutput, d);
    }
}

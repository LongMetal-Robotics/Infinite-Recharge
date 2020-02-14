package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.longmetal.Constants;

public class Shooter extends Subsystem {
    private CANSparkMax drum;
    private TalonSRX mSingulator; // this is the motor that has the mec wheels attached

    public Shooter(boolean setEnabled) {
        super(setEnabled);
    }

    public void init() {
        drum = new CANSparkMax(Constants.kP_DRUM, MotorType.kBrushless);
        drum.set(0);
        mSingulator = new TalonSRX(Constants.kP_SINGULATOR);
        drum.setOpenLoopRampRate(1);

        super.init();
    }
}

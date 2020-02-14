package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.longmetal.Constants;

public class Intake extends Subsystem {
    private TalonSRX mIntake;
    private CANSparkMax mTransport;

    public Intake(boolean setEnabled) {
        super(setEnabled);
    }

    public void init() {
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mTransport = new CANSparkMax(Constants.kP_TRANSPORT, MotorType.kBrushless);
        mTransport.setOpenLoopRampRate(1);

        super.init();
    }
}

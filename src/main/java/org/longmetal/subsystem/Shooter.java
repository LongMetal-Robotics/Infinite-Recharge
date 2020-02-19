package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.longmetal.Constants;
import org.longmetal.exception.SubsystemException;

public class Shooter extends Subsystem {
    private CANSparkMax drum;
    private TalonSRX mSingulator; // this is the motor that has the mec wheels attached
    private CANEncoder drumEncoder;

    public Shooter(boolean setEnabled) {
        super(setEnabled);
        if (setEnabled) { // I'm dumb and they way I wrote the inheritance, it wouldn't work (it
            // wouldn't call the right init).
            init();
        }
    }

    public void init() {
        drum = new CANSparkMax(Constants.kP_SHOOTER, MotorType.kBrushless);
        drumEncoder = new CANEncoder(drum);
        drum.set(0);
        mSingulator = new TalonSRX(Constants.kP_SINGULATOR);
        drum.setOpenLoopRampRate(1);

        super.init();
    }

    public void testShooter(double lTrigger) throws SubsystemException {
        check();
        drum.set(lTrigger);
        System.out.println(drumEncoder.getVelocity());
    }

    public void setSingulatorSpeed(int i) throws SubsystemException {
        check();
        mSingulator.set(ControlMode.PercentOutput, Constants.kSINGULATOR_SPEED * i);
    }
}

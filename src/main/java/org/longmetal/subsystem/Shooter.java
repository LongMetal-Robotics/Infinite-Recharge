package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import org.longmetal.Constants;

public class Shooter extends Subsystem {
    private CANSparkMax drum;
    public CANPIDController drumPID;
    public CANEncoder drumEncoder;
    private TalonSRX mSingulator; // this is the motor that has the mec wheels attached

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, minRPM, acceptableDiff = 50;

    public Shooter(boolean setEnabled) {
        super(setEnabled);
        // if (setEnabled) { // I'm dumb and they way I wrote the inheritance, it wouldn't work (it
        //     // wouldn't call the right init).
        //     init();
        // }
    }

    public void init() {
        drum = new CANSparkMax(Constants.kP_SHOOTER, MotorType.kBrushless);
        drumEncoder = new CANEncoder(drum);
        drum.set(0);

        kP = 0.00075;
        kI = 0.000001;
        kD = 0.000023;
        kIz = 0;
        kFF = 0.000015;
        kMaxOutput = 1;
        kMinOutput = 0;
        maxRPM = 5000;
        minRPM = 0;
        drumPID = drum.getPIDController();
        drumPID.setP(kP);
        drumPID.setI(kI);
        drumPID.setD(kD);
        drumPID.setIZone(kIz);
        drumPID.setFF(kFF);
        drumPID.setOutputRange(kMinOutput, kMaxOutput);
        mSingulator = new TalonSRX(Constants.kP_SINGULATOR);
        drum.setOpenLoopRampRate(1);

        super.init();
    }

    // public void testShooter(double lTrigger) throws SubsystemException {
    //     check();
    //     drum.set(lTrigger);
    //     System.out.println(drumEncoder.getVelocity());
    // }

    public void runShooter(double d) {
        drum.set(d);
    }

    public void stop() {
        drum.set(0);
    }

    public void setSingulatorSpeed(double d) {
        mSingulator.set(ControlMode.PercentOutput, d);
    }

    public double getSpeed() {
        return drumEncoder.getVelocity();
    }

    public void setShooterRPM(double velocity) {
        drumPID.setReference(velocity, ControlType.kVelocity);
    }
}

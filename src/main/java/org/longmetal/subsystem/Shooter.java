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
    private static CANSparkMax drum;
    public static CANPIDController drumPID;
    public static CANEncoder drumEncoder;
    private static TalonSRX mSingulator; // this is the motor that has the mec wheels attached

    public static double kP,
            kI,
            kD,
            kIz,
            kFF,
            kMaxOutput,
            kMinOutput,
            maxRPM,
            minRPM,
            acceptableDiff = 50;

    public static void init() {
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
    }

    // public void testShooter(double lTrigger) throws SubsystemException {
    //     check();
    //     drum.set(lTrigger);
    //     System.out.println(drumEncoder.getVelocity());
    // }

    public static void runShooter(double d) {
        drum.set(d);
    }

    public static void stop() {
        drum.set(0);
    }

    public static void setSingulatorSpeed(double d) {
        mSingulator.set(ControlMode.PercentOutput, d);
    }

    public static double getSpeed() {
        return drumEncoder.getVelocity();
    }

    public static void setShooterRPM(double velocity) {
        drumPID.setReference(velocity, ControlType.kVelocity);
    }
}

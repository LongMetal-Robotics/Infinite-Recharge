package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import org.longmetal.Constants;
import org.longmetal.util.Delay;

public class Intake extends Subsystem {
    private static TalonSRX mIntake;
    private static TalonSRX mHopper;
    private static Timer timer;

    public static void init() {
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mHopper = new TalonSRX(Constants.kP_HOPPER);
        timer = new Timer();
        timer.start();
    }

    public static void setIntakeSpeed(double rTrigger) {
        mIntake.set(ControlMode.PercentOutput, rTrigger);
    }

    public static void setHopperSpeed(double d) {
        mHopper.set(ControlMode.PercentOutput, -d);
    }

    public static Delay runHopper(double d) {
        mHopper.set(ControlMode.PercentOutput, -d);

        return new Delay(
                new Runnable() {

                    @Override
                    public void run() {
                        mHopper.set(ControlMode.PercentOutput, 0);
                    }
                },
                6000);
    }
}

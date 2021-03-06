package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import org.longmetal.Constants;
import org.longmetal.util.Delay;

public class Intake extends Subsystem {
    private TalonSRX mIntake;
    private TalonSRX mHopper;
    private Timer timer;

    public Intake(boolean setEnabled) {
        super(setEnabled);
        // if (setEnabled) { // I'm dumb and they way I wrote the inheritance, it wouldn't work (it
        //     // wouldn't call the right init).
        //     init();
        // }
    }

    public void init() {
        mIntake = new TalonSRX(Constants.kP_INTAKE);
        mHopper = new TalonSRX(Constants.kP_HOPPER);
        timer = new Timer();
        timer.start();

        super.init();
    }

    public void setIntakeSpeed(double rTrigger) {
        mIntake.set(ControlMode.PercentOutput, rTrigger);
    }

    public void setHopperSpeed(double d) {
        mHopper.set(ControlMode.PercentOutput, -d);
    }

    public void runHopper(double d) {
        mHopper.set(ControlMode.PercentOutput, -d);

        Delay.delay(
                new Runnable() {

                    @Override
                    public void run() {
                        mHopper.set(ControlMode.PercentOutput, 0);
                    }
                },
                6000);
    }
}

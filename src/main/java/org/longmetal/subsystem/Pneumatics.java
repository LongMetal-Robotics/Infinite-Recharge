package org.longmetal.subsystem;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.longmetal.Constants;
import org.longmetal.util.Console;

public class Pneumatics extends Subsystem {
    // instance variables
    private static DoubleSolenoid rotator;
    private static DoubleSolenoid drumSpin;

    private static Compressor compressor;
    private static boolean armUp = false;

    public static void init() {
        compressor = new Compressor(Constants.kP_PCM);
        compressor.start();
        compressor.setClosedLoopControl(true);

        // Solenoid to flip up arm
        rotator = new DoubleSolenoid(Constants.kC_PANEL1, Constants.kC_PANEL2);
        rotator.set(kOff);

        drumSpin = new DoubleSolenoid(Constants.kC_CLIMB1, Constants.kC_CLIMB2);
        drumSpin.set(kOff);
    }

    public static void flipArmUp() {
        rotator.set(kForward);
        armUp = true;
        Console.log("armUp = " + armUp);
    }

    public static void flipArmDown() {
        rotator.set(kReverse);
        armUp = false;
        Console.log("armUp = " + armUp);
    }

    // public void setLeftRatchet(boolean on) throws SubsystemException {
    //     check();
    //     drumSpin1.set(on);
    // }

    // public void setRightRatchet(boolean on) throws SubsystemException {
    //     check();
    //     drumSpin2.set(on);
    // }

    public static void setRatchet(boolean on) {
        drumSpin.set(on ? kReverse : kForward);
    }
}

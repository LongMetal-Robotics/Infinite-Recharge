package org.longmetal.subsystem;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import org.longmetal.Constants;
import org.longmetal.exception.SubsystemException;
import org.longmetal.util.Console;

public class Pneumatics extends Subsystem {
    // instance variables
    private DoubleSolenoid rotator; // to rotate the spinner up and down?
    private DoubleSolenoid drumSpin;

    private Compressor compressor;
    private boolean armUp = false;

    public Pneumatics(boolean setEnabled) {
        super(setEnabled);
    }

    public void init() {
        compressor = new Compressor(Constants.kP_PCM);
        compressor.start();
        compressor.setClosedLoopControl(true);

        // Solenoid to flip up arm
        rotator = new DoubleSolenoid(Constants.kC_PANEL1, Constants.kC_PANEL2);

        drumSpin = new DoubleSolenoid(Constants.kC_CLIMB1, Constants.kC_CLIMB2);
        drumSpin.set(kOff);

        super.init();
    }

    public void flipArmUp() throws SubsystemException {
        check();
        rotator.set(kForward);
        armUp = true;
        Console.log("armUp = " + armUp);
    }

    public void flipArmDown() throws SubsystemException {
        check();
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

    public void setRatchet(boolean on) throws SubsystemException {
        check();
        drumSpin.set(on ? kReverse : kForward);
    }
}

package org.longmetal.subsystem;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.longmetal.Constants;
import org.longmetal.exception.SubsystemException;
import org.longmetal.util.Console;

public class Pneumatics extends Subsystem {
    // instance variables
    private DoubleSolenoid rotator; // to rotate the spinner up and down?
    private Solenoid drumSpin1;
    private Solenoid drumSpin2;

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

        drumSpin1 = new Solenoid(1);
        drumSpin2 = new Solenoid(2);
        drumSpin1.set(false);
        drumSpin2.set(false);

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

    public void setLeftRatchet(boolean on) throws SubsystemException {
        check();
        drumSpin1.set(on);
    }

    public void setRightRatchet(boolean on) throws SubsystemException {
        check();
        drumSpin2.set(on);
    }

    public void setRatchet(boolean on) throws SubsystemException {
        check();
        setLeftRatchet(on);
        setRightRatchet(on);
    }
}

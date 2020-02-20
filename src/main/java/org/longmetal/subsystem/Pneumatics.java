package org.longmetal.subsystem;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.longmetal.exception.SubsystemException;

public class Pneumatics extends Subsystem {
    // instance variables
    private DoubleSolenoid rotator; // to rotate the spinner up and down?
    private Compressor compressor;
    private boolean armUp = false;

    public Pneumatics(boolean setEnabled) {
        super(setEnabled);
        if (setEnabled) {
            init();
        }
    }

    public void init() {
        // Solenoid to flip up arm
        rotator = new DoubleSolenoid(0, 3);

        compressor = new Compressor();
    }

    public void flipArmUp() throws SubsystemException {
        check();
        rotator.set(DoubleSolenoid.Value.kForward);
        armUp = true;
    }

    public void flipArmDown() throws SubsystemException {
        check();
        rotator.set(DoubleSolenoid.Value.kReverse);
        armUp = false;
    }
}

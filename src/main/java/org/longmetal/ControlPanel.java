package org.longmetal;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

import org.longmetal.exception.SubsystemException;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class ControlPanel {
    private TalonSRX mSpinner;
    private DoubleSolenoid rotator;
    private ColorSensorV3 csensor;

    private static boolean enabled = true;
    private boolean initialized = false;

    public ControlPanel(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println(
                    "[WARN]\tControl Panel wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        // csensor = new ColorSensorV3
        mSpinner = new TalonSRX(Constants.kP_PANEL);
        // Note colorsensor has its own port but idk how to access it.
        initialized = true;
    }

    public void setSpinnerSpeed() throws SubsystemException {
        // SubsystemManager.check(enabled, initialized);
        mSpinner.set(ControlMode.PercentOutput, Constants.kCONTROL_PANEL_SPEED);
    }

    public void spinnerStop() throws SubsystemException {
        // SubsystemManager.check(enabled, initialized);
        mSpinner.set(ControlMode.PercentOutput, 0);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
            }
            mSpinner.set(ControlMode.PercentOutput, 0); // Sets mSpinner to a speed of 0
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}

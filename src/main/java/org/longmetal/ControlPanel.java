package org.longmetal;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class ControlPanel {
    // instance variables
    private TalonSRX spinner; // spinner motor
    private DoubleSolenoid rotator; // to rotate the spinner up and down?
    private ColorSensorV3 csensor; // color sensor object

    private static boolean enabled = true;
    private static String lastColor = "Unknown";
    private static String initColor = "Unknown";
    private static int setRotations = -1;
    private static int accumulated = -1;
    private boolean initialized = false;
    private final ColorMatch m_colorMatcher = new ColorMatch();

    public ControlPanel(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println(
                    "[WARN]\tShooter wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        csensor = new ColorSensorV3(Port.kOnboard);
        spinner = new TalonSRX(Constants.kP_PANEL);
        // Note colorsensor has its own port but idk how to access it.
        initialized = true;
        spinner.setNeutralMode(NeutralMode.Brake); // sets brake mode so we stop on color
        // adds target values to color matcher for blue, green, red, and yellow
        m_colorMatcher.addColorMatch(Constants.kBlueTarget);
        m_colorMatcher.addColorMatch(Constants.kGreenTarget);
        m_colorMatcher.addColorMatch(Constants.kRedTarget);
        m_colorMatcher.addColorMatch(Constants.kYellowTarget);
    }

    private String currentColor() {
        Color detectedColor = csensor.getColor(); // current color from sensor
        String colorString;
        ColorMatchResult match =
                m_colorMatcher.matchClosestColor(detectedColor); // closest color to one from sensor

        // identifies color
        if (match.color == Constants.kBlueTarget) {
            colorString = "Blue";
        } else if (match.color == Constants.kRedTarget) {
            colorString = "Red";
        } else if (match.color == Constants.kGreenTarget) {
            colorString = "Green";
        } else if (match.color == Constants.kYellowTarget) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }
        return colorString;
    }

    // identifies if color is specified color
    private boolean isColor(String color) {
        String thisColor = currentColor();
        if (thisColor.equals(color)) return true;
        else return false;
    }

    private void spin() {
        spinner.set(
                ControlMode.PercentOutput, Constants.k_SPINRATE); // spins motor at constant speed
    }

    private void stop() {
        spinner.set(ControlMode.PercentOutput, 0.0); // Hard stop
    }

    // spins and returns false if not right color, or stops and returns true if right color
    public boolean spinTo(String color) {
        if (!isColor(color)) {
            spin();
            return false;
        } else {
            stop();
            return true;
        }
    }

    // spins with a new value
    public boolean rotatedSpinTo(String color) {
        return spinTo(color);
    }

    // sets values for rotation
    public void initRotate(int turns) {
        setRotations = turns * 2;
        accumulated = 0;
        lastColor = currentColor();
        initColor = currentColor();
    }

    // while spinning, updates the number of turns so you stop in the right place
    public boolean updateRotate() {
        String currentColor = currentColor();
        if (currentColor != lastColor) {
            lastColor = currentColor;
            if (currentColor == initColor) {
                accumulated++;
                if (accumulated >= setRotations) {
                    stop();
                    return true; // returns true if it did it right?
                }
            }
        }
        spin();
        return false;
    }
}

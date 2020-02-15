package org.longmetal.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import org.longmetal.Constants;
import org.longmetal.exception.SubsystemException;

public class ControlPanel extends Subsystem {
    // instance variables
    private TalonSRX spinner; // spinner motor
    private DoubleSolenoid rotator; // to rotate the spinner up and down?
    private ColorSensorV3 csensor; // color sensor object

    private static PanelColor lastColor = PanelColor.Unknown;
    private static PanelColor initColor = PanelColor.Unknown;
    private static int setRotations = -1;
    private static int accumulated = -1;
    private final ColorMatch m_colorMatcher = new ColorMatch();

    public ControlPanel(boolean setEnabled) {
        super(setEnabled);
    }

    public void init() {
        csensor = new ColorSensorV3(Port.kOnboard);
        spinner = new TalonSRX(Constants.kP_PANEL);
        // Note colorsensor has its own port but idk how to access it.
        spinner.setNeutralMode(NeutralMode.Brake); // sets brake mode so we stop on color
        // adds target values to color matcher for blue, green, red, and yellow
        m_colorMatcher.addColorMatch(Constants.kBlueTarget);
        m_colorMatcher.addColorMatch(Constants.kGreenTarget);
        m_colorMatcher.addColorMatch(Constants.kRedTarget);
        m_colorMatcher.addColorMatch(Constants.kYellowTarget);

        super.init();
    }

    private PanelColor currentColor() {
        Color detectedColor = csensor.getColor(); // current color from sensor
        ColorMatchResult match =
                m_colorMatcher.matchClosestColor(detectedColor); // closest color to one from sensor

        // identifies color
        if (match.color == Constants.kBlueTarget) {
            return PanelColor.Blue;
        } else if (match.color == Constants.kRedTarget) {
            return PanelColor.Red;
        } else if (match.color == Constants.kGreenTarget) {
            return PanelColor.Green;
        } else if (match.color == Constants.kYellowTarget) {
            return PanelColor.Yellow;
        } else {
            return PanelColor.Unknown;
        }
    }

    // identifies if color is specified color
    private boolean isColor(PanelColor color) {
        PanelColor thisColor = currentColor();
        return thisColor == color;
    }

    private void spin() {
        spinner.set(
                ControlMode.PercentOutput, Constants.k_SPINRATE); // spins motor at constant speed
    }

    private void stop() {
        spinner.set(ControlMode.PercentOutput, 0.0); // Hard stop
    }

    // spins and returns false if not right color, or stops and returns true if right color
    public boolean spinTo(PanelColor color) throws SubsystemException {
        check();
        if (!isColor(color)) {
            spin();
            return false;
        } else {
            stop();
            return true;
        }
    }

    // spins with a new value
    public boolean rotatedSpinTo(PanelColor color) throws SubsystemException {
        return spinTo(color);
    }

    // sets values for rotation
    public void initRotate(int turns) throws SubsystemException {
        check();
        setRotations = turns * 2;
        accumulated = 0;
        lastColor = currentColor();
        initColor = currentColor();
    }

    // while spinning, updates the number of turns so you stop in the right place
    public boolean updateRotate() throws SubsystemException {
        check();
        PanelColor currentColor = currentColor();
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

    public enum PanelColor {
        Blue,
        Red,
        Green,
        Yellow,
        Unknown
    }
}

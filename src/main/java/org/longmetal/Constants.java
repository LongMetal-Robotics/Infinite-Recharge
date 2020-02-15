package org.longmetal;

import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;

public class Constants {
    // driving
    // drive train
    // Drive Train
    public static final double kMAX_SPEED_MULT = 0.8; // Limit max speed, was 0.5
    public static final double kSPEED_MODIFIER = 0.7; // Speed modifier (on throttle)
    public static final double kTHROTTLE_SHIFT = 1.05; // Shift throttle up
    public static final double kCURVE_MODIFIER = -0.25; // Curve modifier

    // Spark Max CAN IDs
    public static final int kP_REAR_LEFT = 2;
    public static final int kP_FRONT_LEFT = 1;
    public static final int kP_REAR_RIGHT = 4;
    public static final int kP_FRONT_RIGHT = 3;
    // input
    // Input
    public static final double kINPUT_DEADBAND =
            0.1; // If the value hasn't changed by more than this much we'll ignore it
    // Drive joysticks
    // ports
    public static final int kP_LEFT_STICK = 0; // Left joystick port
    public static final int kP_RIGHT_STICK = 1; // Right joystick port
    // buttons
    public static final int kFORWARD_BUTTON = 5; // Forward button
    public static final int kREVERSE_BUTTON = 3; // Reverse button

    // color wheel
    // TalonSRX
    public static final double kP_CONTROLPANEL = 10; // TalonSRX for spinner (Color Wheel)

    // Colors
    public static final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public static final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public static final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    public static final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    // Climb
    // Solenoid Channels
    public static final int kC_CLIMB1 = 0;
    public static final int kC_CLIMB2 = 1;
    // CAN IDs
    public static final int kP_WINCH1 = 5;
    public static final int kP_WINCH2 = 6;

    // Manipulator
    // CAN IDs
    public static final int kP_INTAKE = 7;
    public static final int kP_TRANSPORT = 8;
    public static final int kP_DRUM = 9;
    public static final int kP_SINGULATOR =
            13; // Temporarily 13 cuz I set the PDP and PCM to 11 and 12
    /* Will be fixed eventually :) -Ben
    when is eventually? -Sky
    */

    // Control Panel
    // CAN IDs
    public static final int kP_PANEL = 10;
    public static final double k_SPINRATE = 0.2;

    // gamepad port
    public static final int kP_GAMEPAD = 2;

    // Subsystem Management
    public static final String SHOOTER_KEY = "Enable Shooter";
    public static final String SHOOTER_STATE_KEY = "Shooter Enabled";
    public static final String INTAKE_KEY = "Enable Intake";
    public static final String INTAKE_STATE_KEY = "Intake Enabled";
    public static final String CLIMB_KEY = "Enable Climb";
    public static final String CLIMB_STATE_KEY = "Climb Enabled";

    public static final String ENABLED = "Enabled";
    public static final String DISABLED = "Disabled";
}

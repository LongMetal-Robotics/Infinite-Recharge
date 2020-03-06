package org.longmetal;

import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;

public class Constants {

    // All CAN IDs
    // Drivetrain CAN IDs
    public static final int kP_FRONT_LEFT = 1;
    public static final int kP_REAR_LEFT = 2;
    public static final int kP_FRONT_RIGHT = 3;
    public static final int kP_REAR_RIGHT = 4;

    // Climb CAN IDs
    public static final int kP_LWINCH = 5;
    public static final int kP_RWINCH = 6;

    // Manipulator CAN IDs
    public static final int kP_SHOOTER = 7;
    public static final int kP_SINGULATOR = 8;
    public static final int kP_HOPPER = 9;
    public static final int kP_INTAKE = 10;

    // Control Panel CAN IDs
    public static final int kP_PANEL = 11;

    // Reference:
    public static final int kP_PCM = 0;
    // PDP = CAN ID 13

    // Solenoid Channels
    public static final int kC_CLIMB1 = 5;
    public static final int kC_CLIMB2 = 4;
    public static final int kC_PANEL1 = 0;
    public static final int kC_PANEL2 = 1;

    // Input
    // Gamepad Input Ports/Axis/Buttons
    // Gamepad Port
    public static final int kP_GAMEPAD = 2;

    public static final int kY_AXIS_MODIFIER = -1;
    // Normally, down is positive. We want up to be positive.

    // Input
    public static final double kINPUT_DEADBAND =
            0.1; // If the value hasn't changed by more than this much we'll ignore it

    // Drive joysticks
    public static final int kP_LEFT_STICK = 0; // Left joystick port
    public static final int kP_RIGHT_STICK = 1; // Right joystick port
    public static final int kFORWARD_BUTTON = 5; // Forward button
    public static final int kREVERSE_BUTTON = 3; // Reverse button

    // Drive Train
    public static final double kMAX_SPEED_MULT = 0.8; // Limit max speed, was 0.5
    public static final double kSPEED_MODIFIER = 0.7; // Speed modifier (on throttle)
    public static final double kTHROTTLE_SHIFT = 1.05; // Shift throttle up
    public static final double kCURVE_MODIFIER = -0.25; // Curve modifier

    // Shooter
    public static final double kSHOOTER_MIN = 1000; // Min shooter speed
    public static final double kSHOOTER_MAX =
            5000; // Max shooter speed (to protect against excessive amperage or safety I guess)
    public static final double kSHOOTER_SPEED_MODIFIER = 1; // Decrease shooter speed
    // Effect of modifiers on finals speeds
    public static final double kSHOOTER_X_MODIFIER = 0.5;
    public static final double kSHOOTER_Y_MODIFIER = 0.5;
    public static final int kLOW_PORT_REVERSE_TIME = 2000;

    // Singulator / Transport
    public static final double kSINGULATOR_SPEED = 0.6; // singulator speed
    public static final double kTRANSPORT_SPEED = 0.8; // transport speed

    // Control Panel
    public static final double kCONTROL_PANEL_SPEED = 0.4; // Control Panel Speed

    // Control Panel + Climb
    public static final double k_SPINRATE = 0.2;
    public static final double CLIMB_SPEED = 0.3;

    // Colors
    public static final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public static final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public static final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    public static final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    // Limelight
    public static final int PIPELINE_VISION = 0;
    public static final int PIPELINE_DRIVE = 1;
    public static final double LL_ON = 3.0;
    public static final double LL_OFF = 0.0;

    // Preferences
    public static final String kSHOOTER_KEY = "SHOOTER";
    public static final String kINTAKE_KEY = "INTAKE";
    public static final String kPANEL_KEY = "PANEL";
    public static final String kCLIMB_KEY = "CLIMB";

    // Subsystem Management
    public static final String SHOOTER_KEY = "Enable Shooter";
    public static final String SHOOTER_STATE_KEY = "Shooter Enabled";
    public static final String INTAKE_KEY = "Enable Intake";
    public static final String INTAKE_STATE_KEY = "Intake Enabled";
    public static final String CLIMB_KEY = "Enable Climb";
    public static final String CLIMB_STATE_KEY = "Climb Enabled";
    public static final String PANEL_STATE_KEY = "Panel Enabled";
    public static final String PANEL_KEY = "Enable Panel";

    // String literals for multiple reuses
    public static final String kENABLED = "Enabled";
    public static final String kDISABLED = "Disabled";
}

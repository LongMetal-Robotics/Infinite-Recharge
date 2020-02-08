package org.longmetal;

public class Constants {
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

    // Input
    public static final double kINPUT_DEADBAND =
            0.1; // If the value hasn't changed by more than this much we'll ignore it
    // Drive joysticks
    public static final int kP_LEFT_STICK = 0; // Left joystick port
    public static final int kP_RIGHT_STICK = 1; // Right joystick port

    public static final int kFORWARD_BUTTON = 5; // Forward button
    public static final int kREVERSE_BUTTON = 3; // Reverse button

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
    public static final int kP_SINGULATOR = 13;
    // Temporarily 13 cuz I set the PDP and PCM to 11 and 12
    // Will be fixed eventually :) -Ben

    // Shooter
    public static final String kSHOOTER_STATE_KEY = "Shooter Enabled"; // SmartDashboard value key
    public static final String kSHOOTER_ENABLER_KEY = "Enable Shooter";

    // Intake
    public static final String kINTAKE_STATE_KEY = "Intake Enabled"; // SmartDashboard value key
    public static final String kINTAKE_ENABLER_KEY = "Enable Intake";

    // Preferences
    public static final String kSHOOTER_KEY = "SHOOTER";
    public static final String kINTAKE_KEY = "INTAKE";

    // String literals for multiple reuses
    public static final String kENABLED = "Enabled";
    public static final String kDISABLED = "Disabled";

    // Control Panel
    // CAN IDs
    public static final int kP_PANEL = 10;

    public static final int kP_GAMEPAD = 2; // Gamepad port
    public static final int kA_TRIGGER = 3; // Trigger axis number
    public static final int kA_LS_X = 0;    // Left stick X axis #
    public static final int kA_LS_Y = 1;    // Left stick Y axis #
    public static final int kA_RS_Y = 5;    // Right stick Y axis #
    public static final int kY_AXIS_MODIFIER = -1;  // Normally, down is positive. We want up to be positive.
    
}

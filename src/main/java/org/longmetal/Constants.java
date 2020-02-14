package org.longmetal;

public class Constants {
    
    // All CAN IDs
    // Drivetrain CAN IDs
    public static final int kP_FRONT_LEFT = 1;
    public static final int kP_REAR_LEFT = 2;
    public static final int kP_FRONT_RIGHT = 3;
    public static final int kP_REAR_RIGHT = 4;

    // Climb CAN IDs
    public static final int kP_WINCH1 = 5;
    public static final int kP_WINCH2 = 6;

    // Manipulator CAN IDs
    public static final int kP_SHOOTER = 7;
    public static final int kP_SINGULATOR = 8;
    public static final int kP_HOPPER = 9;
    public static final int kP_INTAKE = 10;
 
    // Control Panel CAN IDs
    public static final int kP_PANEL = 11;

    // Reference:
    // PCM = CAN ID 12
    // PDP = CAN ID 13

    // Climb
    // Solenoid Channels
    public static final int kC_CLIMB1 = 0;
    public static final int kC_CLIMB2 = 1;




    // Input
    // Gamepad Input Ports/Axis/Buttons
    // Gamepad Port
    public static final int kP_GAMEPAD = 2;

    // Buttons
    public static final int kB_A = 0; // A button 
    public static final int kB_B = 1; // B button (Shooter aim/speed)
    public static final int kB_X = 2; // X button
    public static final int kB_Y = 3; // Y button (Control panel mode)
    public static final int kB_LB = 4; // LB button number (Shooter idle)
    public static final int kB_RB = 5; // RB button number (Intake reverse [release climb])
    public static final int kB_BACK = 6; // Back button
    public static final int kB_START = 7; // Start button
    public static final int kB_LS_PRESS = 8; // Left stick press button
    public static final int kB_RS_PRESS = 9; // Right stick press button

    // Axis
    public static final int kA_LS_X = 0; // Left stick X axis # 
    public static final int kA_LS_Y = 1; // Left stick Y axis # (Left winch up/down)
    public static final int kA_LEFT_TRIGGER = 2; // Left trigger axis # (Shooter feed)
    public static final int kA_RIGHT_TRIGGER = 3; // Right trigger axis # (Intake control)
    public static final int kA_RS_X = 4; // Right stick X axis #
    public static final int kA_RS_Y = 5; // Right stick Y axis # (Right winch up/down)
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
    public static final String kSHOOTER_STATE_KEY = "Shooter Enabled"; // SmartDashboard value key
    public static final String kSHOOTER_ENABLER_KEY = "Enable Shooter";
    public static final double kSHOOTER_MIN = 0.1; // Min shooter speed
    public static final double kSHOOTER_MAX =
            0.75; // Max shooter speed (to protect against excessive amperage or safety I guess)
    public static final double kSHOOTER_SPEED_MODIFIER = 1; // Decrease shooter speed
    // Effect of modifiers on finals speeds
    public static final double kSHOOTER_X_MODIFIER = 0.5;
    public static final double kSHOOTER_Y_MODIFIER = 0.5;

    // Singulator
    public static final double kSINGULATOR_SPEED = 0.4; // singulator speed

    // Intake
    public static final String kINTAKE_STATE_KEY = "Intake Enabled"; // SmartDashboard value key
    public static final String kINTAKE_ENABLER_KEY = "Enable Intake";

    // Preferences
    public static final String kSHOOTER_KEY = "SHOOTER";
    public static final String kINTAKE_KEY = "INTAKE";
    public static final String kCONTROL_PANEL_KEY = "CONTROL PANEL";
    public static final String kCLIMB_KEY = "CLIMB";

    // String literals for multiple reuses
    public static final String kENABLED = "Enabled";
    public static final String kDISABLED = "Disabled";

    // Control Panel
    public static final String kCONTROL_PANEL_STATE_KEY = "CP Enabled";
    public static final String kCONTROL_PANEL_ENABLER_KEY = "Enable CP";
    

    
}

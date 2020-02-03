package org.longmetal;

public class Constants {
    // Drive Train
    public static final double kMAX_SPEED_MULT = 0.8;   // Limit max speed, was 0.5
    public static final double kSPEED_MODIFIER = 0.7;   // Speed modifier (on throttle)
    public static final double kTHROTTLE_SHIFT = 1.05;  // Shift throttle up
    public static final double kCURVE_MODIFIER = -0.25; // Curve modifier
    // Spark Max CAN IDs
    public static final int kP_REAR_LEFT = 2;
    public static final int kP_FRONT_LEFT = 1;
    public static final int kP_REAR_RIGHT = 4;
    public static final int kP_FRONT_RIGHT = 3;

    // Input
    public static final double kINPUT_DEADBAND = 0.1;   // If the value hasn't changed by more than this much we'll ignore it
    // Drive joysticks
    public static final int kP_LEFT_STICK = 0;  // Left joystick port
    public static final int kP_RIGHT_STICK = 1; // Right joystick port
    public static final int kFORWARD_BUTTON = 5;    // Forward button
    public static final int kREVERSE_BUTTON = 3;    // Reverse button
}
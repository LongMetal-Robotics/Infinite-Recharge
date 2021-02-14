package org.longmetal.input;

import org.longmetal.Constants;

import edu.wpi.first.wpilibj.Joystick;

public class Input {
    private static int leftStickPortInit, rightStickPortInit;
    public static Joystick forwardStick, turnStick;
    public static Gamepad gamepad;
    private static boolean isQuinnDrive = false;
    // private boolean isTechDrive = false;

    public static void init() {
        leftStickPortInit = Constants.kP_LEFT_STICK;
        rightStickPortInit = Constants.kP_RIGHT_STICK;
        forwardStick = new Joystick(leftStickPortInit);
        turnStick = new Joystick(rightStickPortInit);
        gamepad = new Gamepad(Constants.kP_GAMEPAD);
    }

    public static void setQuinnDrive(boolean doIt) {
        if (doIt) {
            forwardStick = new Joystick(rightStickPortInit);
            turnStick = new Joystick(leftStickPortInit);
        } else {
            forwardStick = new Joystick(leftStickPortInit);
            turnStick = new Joystick(rightStickPortInit);
        }
        isQuinnDrive = doIt;
    }

    public static boolean isQuinnDrive() {
        return isQuinnDrive;
    }
}

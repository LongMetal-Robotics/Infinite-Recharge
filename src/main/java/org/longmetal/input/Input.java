package org.longmetal.input;

import org.longmetal.Constants;

public class Input {
    private static int leftStickPortInit, rightStickPortInit;
    public static DriveStick forwardStick, turnStick;
    public static Gamepad gamepad;
    private static boolean isQuinnDrive = false;
    // private boolean isTechDrive = false;

    public static void init() {
        leftStickPortInit = Constants.kP_LEFT_STICK;
        rightStickPortInit = Constants.kP_RIGHT_STICK;
        forwardStick = new DriveStick(leftStickPortInit);
        turnStick = new DriveStick(rightStickPortInit);
        gamepad = new Gamepad(Constants.kP_GAMEPAD);
    }

    public static void setQuinnDrive(boolean doIt) {
        if (doIt) {
            forwardStick = new DriveStick(rightStickPortInit);
            turnStick = new DriveStick(leftStickPortInit);
        } else {
            forwardStick = new DriveStick(leftStickPortInit);
            turnStick = new DriveStick(rightStickPortInit);
        }
        isQuinnDrive = doIt;
    }

    public static boolean isQuinnDrive() {
        return isQuinnDrive;
    }

    /*public void setTechDrive(boolean doIt) {

    }*/
}

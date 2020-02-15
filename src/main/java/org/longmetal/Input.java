package org.longmetal;

import org.longmetal.util.DriveStick;
import org.longmetal.util.Gamepad;

public class Input {
    private int leftStickPortInit, rightStickPortInit;
    public DriveStick forwardStick, turnStick;
    public Gamepad gamepad;
    private boolean isQuinnDrive = false;
    // private boolean isTechDrive = false;

    public Input() {
        leftStickPortInit = Constants.kP_LEFT_STICK;
        rightStickPortInit = Constants.kP_RIGHT_STICK;
        forwardStick = new DriveStick(leftStickPortInit);
        turnStick = new DriveStick(rightStickPortInit);
        gamepad = new Gamepad(Constants.kP_GAMEPAD);
    }

    public void setQuinnDrive(boolean doIt) {
        if (doIt) {
            forwardStick = new DriveStick(rightStickPortInit);
            turnStick = new DriveStick(leftStickPortInit);
        } else {
            forwardStick = new DriveStick(leftStickPortInit);
            turnStick = new DriveStick(rightStickPortInit);
        }
        isQuinnDrive = doIt;
    }

    public boolean isQuinnDrive() {
        return isQuinnDrive;
    }

    /*public void setTechDrive(boolean doIt) {

    }*/
}

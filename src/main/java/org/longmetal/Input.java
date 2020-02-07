package org.longmetal;

import edu.wpi.first.wpilibj.Joystick;

public class Input {
    private int leftStickPortInit, rightStickPortInit;
    public Joystick forwardStick, turnStick /*, gamepad*/;
    private boolean isQuinnDrive = false;
    //private boolean isTechDrive = false;

    public Input() {
        leftStickPortInit = Constants.kP_LEFT_STICK;
        rightStickPortInit = Constants.kP_RIGHT_STICK;
        forwardStick = new Joystick(leftStickPortInit);
        turnStick = new Joystick(rightStickPortInit);
        // gamepad = new Joystick(Constants.kP_GAMEPAD);
    }

    public void setQuinnDrive(boolean doIt) {
        if (doIt) {
            forwardStick = new Joystick(rightStickPortInit);
            turnStick = new Joystick(leftStickPortInit);
        } else {
            forwardStick = new Joystick(leftStickPortInit);
            turnStick = new Joystick(rightStickPortInit);
        }
        isQuinnDrive = doIt;
    }

    public boolean isQuinnDrive() {
        return isQuinnDrive;
    }

    /*public void setTechDrive(boolean doIt) {
        
    }*/
}

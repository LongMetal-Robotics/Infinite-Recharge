package org.longmetal;

import edu.wpi.first.wpilibj.Joystick;

enum DPad{
    LEFT, RIGHT, UP, DOWN, NONE
}

enum Axis{
    LTX, RTX, LTY, RTY, NONE
}

public class Input {
    private int leftStickPortInit, rightStickPortInit;
    public Joystick forwardStick, turnStick, gamepad;
    private boolean isQuinnDrive = false;

    DPad Dpad;
    Axis axis;

    public Input() {
        leftStickPortInit = Constants.kP_LEFT_STICK;
        rightStickPortInit = Constants.kP_RIGHT_STICK;
        forwardStick = new Joystick(leftStickPortInit);
        turnStick = new Joystick(rightStickPortInit);
        gamepad = new Joystick(Constants.kP_GAMEPAD);
    }

    public boolean getX()
    {
        return gamepad.getRawButton(3);
    }
    public boolean getY()
    {
        return gamepad.getRawButton(4);
    }
    public boolean getA()
    {
        return gamepad.getRawButton(1);
    }
    public boolean getB()
    {
        return gamepad.getRawButton(2);
    }
    public DPad getDPad()
    {
        
        if (gamepad.getPOV(0) == 0)
            return DPad.UP;
        if (gamepad.getPOV(0) == 90)
            return DPad.LEFT;
        if (gamepad.getPOV(0) == 180)
            return DPad.DOWN;
        if (gamepad.getPOV(0) == 270)
            return DPad.RIGHT;
        return DPad.NONE;
    }
    public Axis getAxis()
    {
        if(gamepad.getRawAxis(2) < 0.5 || gamepad.getRawAxis(2) > 0.5)
            return Axis.LTY;
        if(gamepad.getRawAxis(2) > 0.5 || gamepad.getRawAxis(2) < -0.5)
            return Axis.LTX;
            if(gamepad.getRawAxis(3) < 0.5 || gamepad.getRawAxis(3) > 0.5)
            return Axis.RTY;
        if(gamepad.getRawAxis(3) > 0.5 || gamepad.getRawAxis(3) < -0.5)
            return Axis.RTX;
        return Axis.NONE;
    }

    public 

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
}

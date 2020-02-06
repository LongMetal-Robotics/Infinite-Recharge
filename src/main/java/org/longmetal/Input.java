package org.longmetal;

import edu.wpi.first.wpilibj.Joystick;

enum DPad{
    LEFT, RIGHT, UP, DOWN, NONE
}

enum Axis{
    LTUP, LTDOWN, LTLEFT, LTRIGHT, RTUP, RTDOWN, RTLEFT, RTRIGHT, NONE;
}

public class Input {
    private int leftStickPortInit, rightStickPortInit;
    public Joystick forwardStick, turnStick, gamepad;
    private boolean isQuinnDrive = false;

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
        if(gamepad.getRawAxis(0) < 0.1)
            return Axis.LTLEFT;
        if(gamepad.getRawAxis(0) > 0.9)
            return Axis.LTRIGHT;
        if(gamepad.getRawAxis(1) < 0.1)
            return Axis.LTUP;
        if(gamepad.getRawAxis(1) > 0.9)
            return Axis.LTDOWN;
        if(gamepad.getRawAxis(5) < 0.1)
            return Axis.RTLEFT;
        if(gamepad.getRawAxis(5) > 0.9)
            return Axis.RTRIGHT;
        if(gamepad.getRawAxis(6) < 0.1)
            return Axis.RTUP;
        if(gamepad.getRawAxis(6) > 0.9)
            return Axis.RTDOWN;
        return Axis.NONE;
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
}

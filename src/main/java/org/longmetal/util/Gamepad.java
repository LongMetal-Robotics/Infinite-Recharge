package org.longmetal.util;

import edu.wpi.first.wpilibj.Joystick;

enum DPad{
    LEFT, RIGHT, UP, DOWN, NONE
}

enum Axis{
    LTUP, LTDOWN, LTLEFT, LTRIGHT, RTUP, RTDOWN, RTLEFT, RTRIGHT, NONE;
}

public class Gamepad extends Joystick
{
    private Joystick gamepad;

    public Gamepad(int port)
    {
        super(port);
        gamepad = new Joystick(port);
    }

    public boolean getXButton()
    {
        return gamepad.getRawButton(3);
    }
    public boolean getYButton()
    {
        return gamepad.getRawButton(4);
    }
    public boolean getAButton()
    {
        return gamepad.getRawButton(1);
    }
    public boolean getBButton()
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

}
package org.longmetal.input;

import edu.wpi.first.wpilibj.Joystick;

enum DPad {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    NONE
}

enum Axis {
    LTUP,
    LTDOWN,
    LTLEFT,
    LTRIGHT,
    RTUP,
    RTDOWN,
    RTLEFT,
    RTRIGHT,
    NONE;
}

public class Gamepad extends Joystick {

    public Gamepad(int port) {
        super(port);
    }

    public boolean getXButton() {
        return getRawButton(3);
    }

    public boolean getYButton() {
        return getRawButton(4);
    }

    public boolean getAButton() {
        return getRawButton(1);
    }

    public boolean getBButton() {
        return getRawButton(2);
    }

    public DPad getDPad() {

        if (getPOV(0) == 0) return DPad.UP;
        if (getPOV(0) == 90) return DPad.LEFT;
        if (getPOV(0) == 180) return DPad.DOWN;
        if (getPOV(0) == 270) return DPad.RIGHT;
        return DPad.NONE;
    }

    public Axis getAxis() {
        if (getRawAxis(0) < 0.1) return Axis.LTLEFT;
        if (getRawAxis(0) > 0.9) return Axis.LTRIGHT;
        if (getRawAxis(1) < 0.1) return Axis.LTUP;
        if (getRawAxis(1) > 0.9) return Axis.LTDOWN;
        if (getRawAxis(5) < 0.1) return Axis.RTLEFT;
        if (getRawAxis(5) > 0.9) return Axis.RTRIGHT;
        if (getRawAxis(6) < 0.1) return Axis.RTUP;
        if (getRawAxis(6) > 0.9) return Axis.RTDOWN;
        return Axis.NONE;
    }
}

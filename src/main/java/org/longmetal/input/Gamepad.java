package org.longmetal.input;

import edu.wpi.first.wpilibj.Joystick;

public class Gamepad extends Joystick {

    public Gamepad(int port) {
        super(port);
    }

    public boolean getButton(Button button) throws EnumConstantNotPresentException {
        switch (button) {
            case A:
                return getRawButton(1);

            case B:
                return getRawButton(2);

            case X:
                return getRawButton(3);

            case Y:
                return getRawButton(4);

            case LB:
                return getRawButton(5);

            case RB:
                return getRawButton(6);

            case BACK:
                return getRawButton(7);

            case START:
                return getRawButton(8);

            case LS:
                return getRawButton(9);

            case RS:
                return getRawButton(10);

            default:
                throw new EnumConstantNotPresentException(Button.class, button.name());
        }
    }

    public DPad getDPad() {
        switch (getPOV(0)) {
            case 0:
                return DPad.UP;

            case 90:
                return DPad.RIGHT;

            case 180:
                return DPad.DOWN;

            case 270:
                return DPad.LEFT;

            default:
                return DPad.NONE;
        }
    }

    public double getAxis(Axis axis) {
        switch (axis) {
            case LS_X:
                return getRawAxis(0);

            case LS_Y:
                return getRawAxis(1);

            case RS_X:
                return getRawAxis(4);

            case RS_Y:
                return getRawAxis(5);

            case LT:
                return getRawAxis(2);

            case RT:
                return getRawAxis(3);

            default:
                return 0;
        }
    }

    public enum DPad {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NONE
    }

    public enum Axis {
        LS_X,
        LS_Y,
        RS_X,
        RS_Y,
        LT,
        RT
    }

    public enum Button {
        A,
        B,
        X,
        Y,
        LB,
        RB,
        BACK,
        START,
        LS,
        RS
    }
}

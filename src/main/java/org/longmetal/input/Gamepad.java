package org.longmetal.input;

import edu.wpi.first.wpilibj.XboxController;

/**
 * A wrapper class for the provided XboxController with some niceities such as getting buttons and
 * axes with an Enum
 */
public class Gamepad extends XboxController {

    public Gamepad(int port) {
        super(port);
    }

    public boolean getButton(Button button) throws EnumConstantNotPresentException {
        switch (button) {
            case A:
                return getAButton();

            case B:
                return getBButton();

            case X:
                return getXButton();

            case Y:
                return getYButton();

            case LB:
                return getBumper(Hand.kLeft);

            case RB:
                return getBumper(Hand.kRight);

            case BACK:
                return getBackButton();

            case START:
                return getStartButton();

            case LS:
                return getStickButton(Hand.kLeft);

            case RS:
                return getStickButton(Hand.kRight);

            default:
                throw new EnumConstantNotPresentException(Button.class, button.name());
        }
    }

    public DPad getDPad() {
        switch (getPOV(0)) {
            case 0:
                return DPad.UP;

            case 45:
                return DPad.UP_RIGHT;

            case 90:
                return DPad.RIGHT;

            case 135:
                return DPad.DOWN_RIGHT;

            case 180:
                return DPad.DOWN;

            case 225:
                return DPad.DOWN_LEFT;

            case 270:
                return DPad.LEFT;

            case 315:
                return DPad.UP_LEFT;

            default:
                return DPad.NONE;
        }
    }

    public double getAxis(Axis axis) {
        switch (axis) {
            case LS_X:
                return getX(Hand.kLeft);

            case LS_Y:
                return getY(Hand.kLeft);

            case RS_X:
                return getX(Hand.kRight);

            case RS_Y:
                return getY(Hand.kRight);

            case LT:
                return getTriggerAxis(Hand.kLeft);

            case RT:
                return getTriggerAxis(Hand.kRight);

            default:
                throw new EnumConstantNotPresentException(Axis.class, axis.name());
        }
    }

    public enum DPad {
        UP,
        UP_RIGHT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT,
        LEFT,
        UP_LEFT,
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

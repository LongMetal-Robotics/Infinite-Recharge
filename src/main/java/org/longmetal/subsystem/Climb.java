package org.longmetal.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.longmetal.Constants;
import org.longmetal.exception.SubsystemException;
import org.longmetal.util.Delay;

public class Climb extends Subsystem {
    private CANSparkMax winch1;
    private CANSparkMax winch2;
    public CANEncoder encoder1;
    public CANEncoder encoder2;
    private boolean winchEnabled = false;
    private boolean waitingWinchEnabled = false;

    public Climb(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void init() {
        winch1 = new CANSparkMax(Constants.kP_WINCH1, MotorType.kBrushless);
        winch2 = new CANSparkMax(Constants.kP_WINCH2, MotorType.kBrushless);
        winch1.set(0);
        winch2.set(0);
        // winch1.setOpenLoopRampRate(1);
        // winch2.setOpenLoopRampRate(1);
        encoder1 = winch1.getEncoder();
        encoder2 = winch2.getEncoder();

        super.init();
    }

    public void setLeftWinchSpeed(double speed) throws SubsystemException {
        check();
        if (speed < 0
                && encoder1.getPosition()
                        <= 1) { // Wants to reel in and is within 1 rotation of home
            winch1.set(0);
        } else {
            winch1.set(speed);
        }
    }

    public void setRightWinchSpeed(double speed) throws SubsystemException {
        check();
        if (speed < 0
                && encoder2.getPosition()
                        <= 1) { // Wants to reel in and is within 1 rotation of home
            winch1.set(0);
        } else {
            winch2.set(-speed);
        }
    }

    public void setWinchSpeed(double speed) throws SubsystemException {
        check();
        setLeftWinchSpeed(speed);
        setRightWinchSpeed(speed);
    }

    public void resetEncoders() throws SubsystemException {
        check();
        encoder1.setPosition(0);
        encoder2.setPosition(0);
    }

    public void setWinchEnabled(boolean enabled) {
        winchEnabled = enabled;
    }

    public boolean getWinchEnabled() {
        return winchEnabled;
    }

    public void delayedEnableWinch() {
        waitingWinchEnabled = true;
        Delay.delay(
                new Runnable() {

                    @Override
                    public void run() {
                        setWinchEnabled(true);
                        waitingWinchEnabled = false;
                    }
                },
                Constants.CLIMB_DELAY);
    }

    public boolean getWaitingWinchEnabled() {
        return waitingWinchEnabled;
    }
}

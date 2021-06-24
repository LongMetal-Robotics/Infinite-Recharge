package org.longmetal.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.longmetal.Constants;
import org.longmetal.util.Delay;

public class Climb extends Subsystem {
    private static CANSparkMax winch1;
    private static CANSparkMax winch2;
    public static CANEncoder encoder1;
    public static CANEncoder encoder2;
    private static boolean winchEnabled = false;
    private static boolean waitingWinchEnabled = false;

    // @Override
    public static void init() {
        initialized = true;
        winch1 = new CANSparkMax(Constants.kP_RWINCH, MotorType.kBrushless);
        winch2 = new CANSparkMax(Constants.kP_LWINCH, MotorType.kBrushless);
        winch1.set(0);
        winch2.set(0);
        // winch1.setOpenLoopRampRate(1);
        // winch2.setOpenLoopRampRate(1);
        encoder1 = winch1.getEncoder();
        encoder2 = winch2.getEncoder();
    }

    public static void setLeftWinchSpeed(double speed) {
        // if (speed < 0){
        //         // && encoder1.getPosition()
        //                 // <= 1) { // Wants to reel in and is within 1 rotation of home
        //     winch1.set(0);
        // } else {
        winch1.set(speed);
        // }
    }

    public static void setRightWinchSpeed(double speed) {
        // if (speed < 0){
        //         // && encoder2.getPosition()
        //                 // <= 1) { // Wants to reel in and is within 1 rotation of home
        //     winch1.set(0);
        // } else {
        winch2.set(-speed);
        // }
    }

    public static void setWinchSpeed(double speed) {
        setLeftWinchSpeed(speed);
        setRightWinchSpeed(speed);
    }

    public static void resetEncoders() {
        encoder1.setPosition(0);
        encoder2.setPosition(0);
    }

    public static void setWinchEnabled(boolean enabled) {
        winchEnabled = enabled;
    }

    public static boolean getWinchEnabled() {
        return winchEnabled;
    }

    public static void delayedEnableWinch() {
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

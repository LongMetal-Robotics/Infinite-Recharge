package org.longmetal.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.longmetal.Constants;

public class Climb extends Subsystem {
    private CANSparkMax winch1;
    private CANSparkMax winch2;

    public Climb(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void init() {
        winch1 = new CANSparkMax(Constants.kP_RWINCH, MotorType.kBrushless);
        winch2 = new CANSparkMax(Constants.kP_LWINCH, MotorType.kBrushless);
        winch1.set(0);
        winch2.set(0);
        // winch1.setOpenLoopRampRate(1);
        // winch2.setOpenLoopRampRate(1);

        super.init();
    }

    public void setLeftWinchSpeed(double speed) {
        winch1.set(speed);
    }

    public void setRightWinchSpeed(double speed) {
        winch2.set(speed);
    }

    public void setWinchSpeed(double speed) {
        setLeftWinchSpeed(speed);
        setRightWinchSpeed(-speed);
    }
}

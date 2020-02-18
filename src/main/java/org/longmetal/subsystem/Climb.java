package org.longmetal.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import org.longmetal.Constants;
import org.longmetal.exception.SubsystemException;

public class Climb extends Subsystem {
    private CANSparkMax winch1;
    private CANSparkMax winch2;
    private Solenoid drumSpin1;
    private Solenoid drumSpin2;

    public Climb(boolean isEnabled) {
        super(isEnabled);
        if (isEnabled) { // I'm dumb and they way I wrote the inheritance, it wouldn't work (it
            // wouldn't call the right init).
            init();
        }
    }

    @Override
    public void init() {
        winch1 = new CANSparkMax(Constants.kP_WINCH1, MotorType.kBrushless);
        winch2 = new CANSparkMax(Constants.kP_WINCH2, MotorType.kBrushless);
        winch1.set(0);
        winch2.set(0);
        winch1.setOpenLoopRampRate(1);
        winch2.setOpenLoopRampRate(1);

        // drumSpin1 = new Solenoid(Constants.kC_CLIMB1);
        // drumSpin2 = new Solenoid(Constants.kC_CLIMB2);

        super.init();
    }

    public void setLeftWinchSpeed(double speed) throws SubsystemException {
        check();
        winch1.set(speed);
    }

    public void setRightWinchSpeed(double speed) throws SubsystemException {
        check();
        winch2.set(speed);
    }

    public void setWinchSpeed(double speed) throws SubsystemException {
        check();
        setLeftWinchSpeed(speed);
        setRightWinchSpeed(speed);
    }

    public void setLeftRatchet(boolean on) throws SubsystemException {
        check();
        drumSpin1.set(on);
    }

    public void setRightRatchet(boolean on) throws SubsystemException {
        check();
        drumSpin2.set(on);
    }

    public void setRatchet(boolean on) throws SubsystemException {
        check();
        setLeftRatchet(on);
        setRightRatchet(on);
    }
}

package org.longmetal.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import org.longmetal.Constants;

public class Climb extends Subsystem {
    private CANSparkMax winch1;
    private CANSparkMax winch2;
    private Solenoid drumSpin1;
    private Solenoid drumSpin2;

    public Climb(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void init() {
        winch1 = new CANSparkMax(Constants.kP_WINCH1, MotorType.kBrushless);
        winch2 = new CANSparkMax(Constants.kP_WINCH2, MotorType.kBrushless);
        winch1.setOpenLoopRampRate(1);
        winch2.setOpenLoopRampRate(1);
        winch1.set(0);
        winch2.set(0);

        drumSpin1 = new Solenoid(Constants.kC_CLIMB1);
        drumSpin2 = new Solenoid(Constants.kC_CLIMB2);

        super.init();
    }
}

package org.longmetal;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;

public class Climb
{
    private CANSparkMax winch1;
    private CANSparkMax winch2;
    private Solenoid drumSpin1;
	private Solenoid drumSpin2;
    private static boolean enabled = true;
    private boolean initialized = false;

    public Climb(boolean setEnabled)
    {
        enabled = setEnabled;
        if (enabled) 
        {
            init();
        } 
        else 
        {
            System.out.println("[WARN]\tClimb wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init()
    {
        winch1 = new CANSparkMax(Constants.kP_WINCH1, MotorType.kBrushless);
        winch2 = new CANSparkMax(Constants.kP_WINCH2, MotorType.kBrushless);
        drumSpin1 = new Solenoid(Constants.kC_CLIMB1);
        drumSpin2 = new Solenoid(Constants.kC_CLIMB2);
        initialized = true;
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) 
            {
                init();
                winch1.set(0);
                winch2.set(0);
            }
        }
    }
}
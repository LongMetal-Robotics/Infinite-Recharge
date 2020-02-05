package org.longmetal;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

public class ControlPanel {
    private TalonSRX spinner;
    private DoubleSolenoid rotator;
    private ColorSensorV3 csensor;

    private static boolean enabled = true;
    private boolean initialized = false;

    public ControlPanel(boolean setEnabled)
    {
        enabled = setEnabled;
        if (enabled) 
        {
            init();
        } 
        else 
        {
            System.out.println("[WARN]\tShooter wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() 
    {
        //csensor = new ColorSensorV3
        //spinner = new TalonSRX(Constants.kP_CONTROLPANEL);
        //Note colorsensor has its own port but idk how to access it.
        initialized = true;
    }

}


